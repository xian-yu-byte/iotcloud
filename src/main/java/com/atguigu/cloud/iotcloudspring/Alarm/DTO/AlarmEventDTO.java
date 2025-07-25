package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEventDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long alarmId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceId;
    private String deviceName;
    private String attributeKey;
    private BigDecimal currentValue;
    private String alertLevel;
    private LocalDateTime triggerTime;
    private String status;
}
