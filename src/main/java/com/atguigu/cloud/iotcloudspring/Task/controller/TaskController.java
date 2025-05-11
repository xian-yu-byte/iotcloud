package com.atguigu.cloud.iotcloudspring.Task.controller;

import com.atguigu.cloud.iotcloudspring.Task.Config.MqttTaskScheduler;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.CreateTaskDTO;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.TaskListItemDTO;
import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.service.TaskService;
import com.atguigu.cloud.iotcloudspring.Task.until.CronUtils;
import com.atguigu.cloud.iotcloudspring.Task.until.RunRequest;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class TaskController {
    @Resource
    private TaskService taskService;

    @Resource
    private MqttTaskScheduler mqttTaskScheduler;


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
        taskService.setEnabled(id, enabled);
        return Result.success();
    }

    @PostMapping("/{id}/run")
    public Result<Void> runTask(
            @PathVariable Long id,
            @RequestBody RunRequest req) {
        Task task = taskService.getById(id);
        String st = task.getScheduleType();

        if ("只运行一次".equals(st)) {
            mqttTaskScheduler.scheduleOnce(id, task.getStartTime());
            task.setNextRunTime(task.getStartTime());
        } else if ("每日定时".equals(st) || "间隔时间重复".equals(st) || "自定义".equals(st)) {
            // 不管是每日，还是自定义 Cron，都直接用 cronExpr
            mqttTaskScheduler.scheduleCron(id, task.getCronExpr());
            LocalDateTime next = CronUtils.nextExecution(task.getCronExpr());
            task.setNextRunTime(next);
        } else if ("关闭定时".equals(st)) {
            // 关闭定时
            mqttTaskScheduler.scheduleOnce(id, LocalDateTime.now());
            task.setNextRunTime(LocalDateTime.now());
        } else {
            return Result.error("不支持的调度类型：" + st);
        }
        task.setStatus("加载中");
        task.setLastRunTime(null);
        taskService.updateById(task);
        return Result.success();
    }

    @DeleteMapping("/{id}/task")
    public Result<Void> deleteTask(@PathVariable long id) {
        boolean success = taskService.deleteTaskById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
}
