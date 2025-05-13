package com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateTaskDTO {
    private Long userId;
    private Long projectId;
    private String taskName;
    private String taskType;
    private String scheduleType;
    private String cronExpr;
    private LocalDateTime startTime;
    private Boolean enabled = false;
    private String operationConfig;
    private String taskDescription;

    // --- 定时按设备列表下发 ---
    private List<Long> deviceIds;

    // --- 定时按设备类型下发 ---
    private List<Long> deviceTypeIds;
}
