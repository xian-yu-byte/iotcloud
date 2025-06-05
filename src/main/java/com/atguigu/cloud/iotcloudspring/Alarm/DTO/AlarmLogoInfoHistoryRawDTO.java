package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmLogoInfoHistoryRawDTO {
    private String deviceName;
    private String attributeKey;
    private String compareOp;
    private Double thresholdValue;
    private Double thresholdLow;
    private Double thresholdHigh;
    private LocalDateTime triggerTime;
}
