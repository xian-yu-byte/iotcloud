package com.atguigu.cloud.iotcloudspring.Task.controller;

import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.CreateTaskDTO;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.TaskListItemDTO;
import com.atguigu.cloud.iotcloudspring.Task.service.TaskService;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class TaskController {
    @Resource
    private TaskService taskService;

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
        List<TaskListItemDTO> dtos = taskService.getTaskList(projectId);
        return Result.success(dtos);
    }
}
