package com.atguigu.cloud.iotcloudspring.Task.pojo;

import com.atguigu.cloud.iotcloudspring.Task.enums.TaskExecutionLogResult;
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
    private Integer durationMs;
    private Integer attempt;
    private String result;
    private String message;
}
