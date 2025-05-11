package com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskListItemDTO {
    private Long id;
    private String taskName;
    private String targetType;
    private String taskType;
    private String scheduleType;
    private LocalDateTime nextRunTime;
    private LocalDateTime createdTime;
    private String relativeTime;
}
