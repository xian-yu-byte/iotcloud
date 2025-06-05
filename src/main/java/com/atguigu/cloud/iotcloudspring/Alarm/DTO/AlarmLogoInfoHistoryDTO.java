package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLogoInfoHistoryDTO {
    private String deviceName;
    private String reason;
    private LocalDateTime triggerTime;
}
