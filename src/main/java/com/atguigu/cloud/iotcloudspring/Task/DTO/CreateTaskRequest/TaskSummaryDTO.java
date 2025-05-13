package com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSummaryDTO {
    private LocalDateTime lastExecuteTime;
    private Integer totalRuns;
}
