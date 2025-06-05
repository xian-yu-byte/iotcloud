package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmRuleDetailDTO {
    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private String targetType;
    private Long targetId;
    private String outerLogic;
    private Integer repeatCount;
    private Integer durationSec;
    private String alertLevel;
    private Boolean enabled;
    private List<AlarmRuleEditDTO> conditions;
}
