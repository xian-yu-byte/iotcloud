package com.atguigu.cloud.iotcloudspring.Alarm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmRule {
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
}
