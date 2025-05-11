package com.atguigu.cloud.iotcloudspring.Task.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private Long userId;
    private Long projectId;
    private String taskName;
    private String taskType;
    private String scheduleType;
    private String cronExpr;
    private LocalDateTime startTime;
    private LocalDateTime nextRunTime;
    private LocalDateTime LastRunTime;
    private Integer MaxRetry;
    private Integer RetryIntervalSec;
    private Boolean enabled;
    private String status;
    private String operationConfig;
    private String taskDescription;
}
