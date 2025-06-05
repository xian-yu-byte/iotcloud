package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmLogoInfoHistorysDTO {
    private String deviceName;
    private String alarmName;
    private String alertLevel;
    private String reason;
    private LocalDateTime triggerTime;
}
