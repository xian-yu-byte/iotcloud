package com.atguigu.cloud.iotcloudspring.Task.Config;

import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;
import com.atguigu.cloud.iotcloudspring.Task.service.TaskService;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttPublisher;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@AllArgsConstructor
@Slf4j
public class MqttTaskScheduler {
    /**
     * 线程池调度器（在 SchedulerConfig 里 @Bean 注入）
     */
    private final ThreadPoolTaskScheduler scheduler;

    /**
     * MQTT 发布器
     */
    private final MqttPublisher mqttPublisher;

    /**
     * 数据/业务服务
     */
    private final TaskService taskService;
    private final DeviceService deviceService;

    /**
     * 存储 taskId -> ScheduledFuture，用于后续取消
     */
    private final Map<Long, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    /* ---------- 调度相关 ---------- */

    /**
     * 安排一次性执行（到点后自动失效，不会重复）
     */
    public void scheduleOnce(Long taskId, LocalDateTime executeTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime execTime = executeTime.isBefore(now) ? executeTime.plusDays(1) : executeTime;
        Instant runAt = execTime.atZone(ZoneId.systemDefault()).toInstant();

        ScheduledFuture<?> f = scheduler.schedule(() -> doPublish(taskId, execTime), runAt);
        futures.put(taskId, f);
        log.info("已注册一次性任务 taskId={}, runAt={}", taskId, execTime);
    }

    /**
     * 安排 Cron 表达式执行（可重复）
     */
    public void scheduleCron(Long taskId, String cronExpr) {
        Trigger trigger = new CronTrigger(cronExpr);
        ScheduledFuture<?> f = scheduler.schedule(
                () -> doPublish(taskId, LocalDateTime.now()), trigger);
        futures.put(taskId, f);
        log.info("已注册循环任务 taskId={}, cron={}", taskId, cronExpr);
    }

    /**
     * 立即执行一次，不进入调度器
     */
    public void executeNow(Long taskId) {
        doPublish(taskId, LocalDateTime.now());
    }

    /**
     * 取消一个任务的所有调度（禁用时调用）
     */
    public void cancel(Long taskId) {
        ScheduledFuture<?> f = futures.remove(taskId);
        if (f != null) {
            f.cancel(false);
            log.info("已取消任务调度 taskId={}", taskId);
        }
    }

    /* ---------- 真正的发布下发 ---------- */

    /**
     * 统一发布方法：拼 topic、发 MQTT、写执行日志
     *
     * @param taskId      任务 ID
     * @param plannedTime 计划执行时间（用于日志）
     */
    private void doPublish(Long taskId, LocalDateTime plannedTime) {
        // 1. 标记“正在运行”
        taskService.updateStatus(taskId, "正在运行");

        // 2. 查任务配置 & 目标
        Task task = taskService.getById(taskId);
        List<TaskTarget> targets = taskService.listTargets(taskId);
        String payload = task.getOperationConfig();

        boolean allSuccess = true;

        // 3. 按 target & device 下发
        for (TaskTarget tgt : targets) {
            List<Long> deviceIds = "DEVICE".equals(tgt.getTargetType())
                    ? List.of(tgt.getTargetId())
                    : deviceService.listDeviceIdsByProjectAndType(
                    task.getProjectId(), tgt.getTargetId());

            for (Long devId : deviceIds) {
                String deviceKey = deviceService.getDeviceKeyById(devId);
                String topic = String.format(
                        "user%d/project%d/device%d/%s",
                        task.getUserId(),
                        task.getProjectId(),
                        devId,
                        deviceKey);

                LocalDateTime start = LocalDateTime.now();
                try {
                    mqttPublisher.publish(topic, payload);
                    LocalDateTime end = LocalDateTime.now();
                    long durationMs = Duration.between(start, end).toMillis();
                    String deviceName = deviceService.getDeviceName(devId);
                    String msg = String.format("设备 “%s” 任务执行成功，下发内容：%s", deviceName, payload);
                    taskService.logExecution(taskId, devId, plannedTime,
                            LocalDateTime.now(), durationMs, "成功", msg);
                } catch (Exception ex) {
                    allSuccess = false;
                    LocalDateTime end = LocalDateTime.now();
                    long durationMs = Duration.between(start, end).toMillis();
                    String deviceName = deviceService.getDeviceName(devId);
                    String msg = String.format("设备 “%s” 任务失败：%s", deviceName, ex.getMessage());
                    taskService.logExecution(taskId, devId, plannedTime,
                            LocalDateTime.now(), durationMs, "失败", msg);
                }
            }
        }

        // 4. 发布完毕，更新 lastRunTime & status
        Task update = new Task();
        update.setId(taskId);
        update.setLastRunTime(LocalDateTime.now());
        update.setStatus(allSuccess ? "成功" : "失败");
        taskService.updateById(update);

        // 5. 如果是一次性任务，执行后自动禁用
        if ("只运行一次".equals(task.getScheduleType())) {
            taskService.setEnabled(taskId, false);
            cancel(taskId);
        }
    }
}

