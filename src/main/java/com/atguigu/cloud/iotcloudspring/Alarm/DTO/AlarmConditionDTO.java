package com.atguigu.cloud.iotcloudspring.Alarm.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlarmConditionDTO {
    private String logicalOp;
    private String attributeKey;
    private String compareOp;

    private BigDecimal thresholdValue;
    private BigDecimal thresholdLow;
    private BigDecimal thresholdHigh;
}
