package com.atguigu.cloud.iotcloudspring.Alarm.pojo;

import com.atguigu.cloud.iotcloudspring.Alarm.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {
    private Long id;
    private Long alarmId;
    private Long deviceId;
    private String attributeKey;
    private BigDecimal currentValue;
    private String alertLevel;
    private LocalDateTime triggerTime;
    private EventStatus status;
    private LocalDateTime clearedTime;
}
