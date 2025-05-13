package com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String taskName;
    private String targetType;
    private String Device;
    private LocalDateTime nextRunTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String taskType;
    private String operationConfig;
    private Boolean enabled;
}
