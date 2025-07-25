package com.atguigu.cloud.iotcloudspring.Task.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskExecutionLog {
    private Long id;
    private Long taskId;
    private Long projectId;
    private Long deviceId;
    private LocalDateTime plannedTime;
    private LocalDateTime executeTime;
    private long durationMs;
    private Integer attempt;
    private String result;
    private String message;
}
