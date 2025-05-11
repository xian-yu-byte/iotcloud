package com.atguigu.cloud.iotcloudspring.Task.Config;

import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;
import com.atguigu.cloud.iotcloudspring.Task.service.TaskService;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttPublisher;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@AllArgsConstructor
public class MqttTaskScheduler {
    private final ThreadPoolTaskScheduler scheduler;
    private final MqttPublisher mqttPublisher;
    private final TaskService taskService;       // 用来查任务定义 & 执行日志
    private final DeviceService deviceService;   // 用来查 deviceKey


    /**
     * 安排一次性执行
     */
    public void scheduleOnce(Long taskId, LocalDateTime executeTime) {
        LocalDateTime now = LocalDateTime.now();
        // 直接用三元表达式，一次性算出要用的时间
        LocalDateTime execTime = executeTime.isBefore(now)
                ? executeTime.plusDays(1)
                : executeTime;

        Instant runAt = execTime.atZone(ZoneId.systemDefault()).toInstant();
        scheduler.schedule(
                () -> doPublish(taskId, execTime),
                runAt
        );
    }

    /**
     * 安排 Cron 表达式执行（可重复）
     */
    public void scheduleCron(Long taskId, String cronExpr) {
        Trigger trigger = new CronTrigger(cronExpr);
        scheduler.schedule(
                () -> doPublish(taskId, LocalDateTime.now()),
                trigger
        );
    }

    /**
     * 下发逻辑：拼 topic、取 payload、发布，并写执行日志
     */
    private void doPublish(Long taskId, LocalDateTime plannedTime) {
        // 1. 标记“正在运行”
        taskService.updateStatus(taskId, "正在运行");

        // 2. 查任务配置和 targets
        Task task = taskService.getById(taskId);
        List<TaskTarget> targets = taskService.listTargets(taskId);
        String payload = task.getOperationConfig();

        boolean allSuccess = true;

        // 3. 按 target & device 下发
        for (TaskTarget tgt : targets) {
            List<Long> deviceIds;
            if ("DEVICE".equals(tgt.getTargetType())) {
                deviceIds = List.of(tgt.getTargetId());
            } else {
                deviceIds = deviceService
                        .listDeviceIdsByProjectAndType(task.getProjectId(), tgt.getTargetId());
            }

            for (Long devId : deviceIds) {
                // 3.1 拼 topic
                String deviceKey = deviceService.getDeviceKeyById(devId);
                String topic = String.format(
                        "user%d/project%d/device%d/%s",
                        task.getUserId(),
                        task.getProjectId(),
                        devId,
                        deviceKey
                );

                // 3.2 发布并记录日志
                try {
                    mqttPublisher.publish(topic, payload);
                    taskService.logExecution(
                            taskId,
                            devId,
                            plannedTime,
                            LocalDateTime.now(),
                            "成功",
                            null
                    );
                } catch (Exception ex) {
                    allSuccess = false;
                    taskService.logExecution(
                            taskId,
                            devId,
                            plannedTime,
                            LocalDateTime.now(),
                            "失败",
                            ex.getMessage()
                    );
                }
            }
        }

        // 4. 发布完毕，更新 last_run_time 和 status
        Task update = new Task();
        update.setId(taskId);
        update.setLastRunTime(LocalDateTime.now());
        update.setStatus(allSuccess ? "成功" : "失败");
        taskService.updateById(update);

        // 5. 如果是一次性任务，禁用它
        if ("只运行一次".equals(task.getScheduleType())) {
            taskService.setEnabled(taskId, false);
        }
    }

}

