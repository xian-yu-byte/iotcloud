package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmRuleEditDTO {
    private Long id;
    private Long alarmId;
    private Integer seq;
    private String logicalOp;
    private String attributeKey;
    private String compareOp;
    private BigDecimal thresholdValue;
    private BigDecimal thresholdLow;
    private BigDecimal thresholdHigh;
}
