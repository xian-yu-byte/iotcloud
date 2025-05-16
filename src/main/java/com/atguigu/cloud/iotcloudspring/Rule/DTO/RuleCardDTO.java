package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.Data;

@Data
public class RuleCardDTO {
    private Long id;
    private String name;
    private String triggerEvent;
    private Boolean enabled;
    private String actionType;
    private String targetType;
}
