package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AlarmRuleCreateDTO {
    private Long  projectId;
    private String name;
    private String description;

    private String targetType;
    private Long   targetId;

    private String outerLogic;
    private Integer repeatCount;
    private Integer durationSec;

    private String alertLevel;
    private Boolean enabled;

    private List<AlarmConditionDTO> conditions;
}
