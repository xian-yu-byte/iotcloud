package com.atguigu.cloud.iotcloudspring.Task.service;

import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.CreateTaskDTO;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.TaskListItemDTO;
import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskPayload;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;

import java.util.List;

public interface TaskService {

    /**
     * 创建新任务（含目标 & 下发内容）
     */
    void createFromRequest(CreateTaskDTO dto);

    /**
     * 获取某项目下的任务列表，并计算 relativeTime
     */
    List<TaskListItemDTO> getTaskList(Long projectId);
}
