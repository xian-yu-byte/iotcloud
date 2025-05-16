package com.atguigu.cloud.iotcloudspring.Task.controller;

import com.atguigu.cloud.iotcloudspring.Task.Config.MqttTaskScheduler;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.*;
import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.service.TaskService;
import com.atguigu.cloud.iotcloudspring.Task.until.CronUtils;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    private final MqttTaskScheduler mqttTaskScheduler;


    @PostMapping("/tasks")
    public Result<Void> create(@RequestBody CreateTaskDTO dto) {
        taskService.createFromRequest(dto);
        return Result.success();
    }

    /**
     * 列表查询：显示 任务名称、目标类型、任务类型、相对执行时间
     */
    @GetMapping("/tasks")
    public Result<List<TaskListItemDTO>> list(@RequestParam Long projectId) {
        List<TaskListItemDTO> dto = taskService.getTaskList(projectId);
        return Result.success(dto);
    }

    /**
     * 切换任务启用/禁用
     */
    @PutMapping("/{id}/enable")
    public Result<Void> toggleEnable(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {

        Boolean enabled = body.get("enabled");
        if (enabled == null) {
            return Result.error("缺少 enabled 参数");
        }

        // 1. 更新 enabled 标志
        taskService.setEnabled(id, enabled);

        // 2. 先取消任何残留调度（保险）
        mqttTaskScheduler.cancel(id);

        if (enabled) {
            Task task = taskService.getById(id);
            String st = task.getScheduleType();

            if ("只运行一次".equals(st)) {
                // 注册一次性任务
                mqttTaskScheduler.scheduleOnce(id, task.getStartTime());
                taskService.updateNextRunTime(id, task.getStartTime());
                taskService.updateStatus(id, "已启用·一次性");
            } else if ("关闭定时".equals(st)) {
                // 不自动调度，纯手动
                taskService.updateNextRunTime(id, null);
                taskService.updateStatus(id, "已启用·手动");
            } else {
                // 循环类型：每日 / 间隔 / 自定义 Cron
                mqttTaskScheduler.scheduleCron(id, task.getCronExpr());
                LocalDateTime next = CronUtils.nextExecution(task.getCronExpr());
                taskService.updateNextRunTime(id, next);
                taskService.updateStatus(id, "已启用·循环");
            }

        } else {
            // 禁用：仅取消调度并清空下次运行时间
            taskService.updateNextRunTime(id, null);
            taskService.updateStatus(id, "已禁用");
        }

        return Result.success();
    }

    /**
     * 手动执行
     */
    @PostMapping("/{id}/runNow")
    public Result<Void> runNow(@PathVariable Long id) {
        // 1. 立即执行一次，不注册到调度器
        mqttTaskScheduler.executeNow(id);

        // 2. 记录手动执行的状态和时间
        Task update = new Task();
        update.setId(id);
        update.setLastRunTime(LocalDateTime.now());
        update.setStatus("手动运行");
        taskService.updateById(update);

        return Result.success();
    }

    @DeleteMapping("/{id}/task")
    public Result<Void> deleteTask(@PathVariable Long id) {
        boolean success = taskService.deleteTaskById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @GetMapping("/{id}/taskDTO")
    public Result<List<TaskDTO>> getTaskDTO(@PathVariable Long id) {
        List<TaskDTO> taskDTO = taskService.selectTaskDTOById(id);
        return Result.success(taskDTO);
    }

    @PutMapping("/{id}/config")
    public Result<Void> updateOperationConfig(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String operationConfig = body.get("operationConfig");
        if (operationConfig == null) {
            return Result.error("缺少 operationConfig 参数");
        }
        taskService.updateOperationConfig(id, operationConfig);
        return Result.success();
    }

//    @PutMapping("/{id}/ScheduleType")
//    public Result<Void> updateScheduleType(@PathVariable Long id, @RequestBody String ScheduleType) {
//
//        taskService.updateScheduleType(id, ScheduleType);
//        return Result.success();
//    }


    @GetMapping("/{taskId}/summary")
    public Result<TaskSummaryDTO> getSummary(@PathVariable Long taskId) {
        TaskSummaryDTO taskIdStr = taskService.selectTaskSummary(taskId);
        return Result.success(taskIdStr);
    }

    @GetMapping("/{taskId}/dailyCounts")
    public Result<List<DayCountDTO>> getTimeCounts(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "7") Integer days) {
        List<DayCountDTO> list = taskService.selectTimeCounts(taskId, days);
        return Result.success(list);
    }

    @GetMapping("/{taskId}/logs")
    public Result<IPage<LogDTO>> getExecutionLogs(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        IPage<LogDTO> page = taskService.getExecutionLogs(taskId, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/{projectId}/logsS")
    public Result<IPage<LogDTO>> getExecutionLogsS(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        IPage<LogDTO> page = taskService.getExecutionLogsS(projectId,pageNum, pageSize);
        return Result.success(page);
    }
}
