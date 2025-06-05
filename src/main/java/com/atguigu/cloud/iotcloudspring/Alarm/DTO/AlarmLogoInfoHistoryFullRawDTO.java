package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmLogoInfoHistoryFullRawDTO {
    // 来自 alarm_rule
    private Long ruleId;
    private String alarmName;
    private String alertLevel;

    // 来自 device
    private String deviceName;

    // 来自 alarm_history/alarm_condition
    private String attributeKey;
    private String compareOp;
    private Double thresholdValue;
    private Double thresholdLow;
    private Double thresholdHigh;

    private LocalDateTime triggerTime;
}
