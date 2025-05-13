package com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {
    private LocalDateTime executeTime;
    private Integer durationMs;
    private Integer attempt;
    private String result;
    private String message;
}
