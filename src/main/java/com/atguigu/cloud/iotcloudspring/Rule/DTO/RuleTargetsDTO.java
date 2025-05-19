package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RuleTargetsDTO {
    private Long id;
    private String triggerEvent;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String targetType;
    private Long targetId;
    private String targetName;
    private Boolean enabled;
}
