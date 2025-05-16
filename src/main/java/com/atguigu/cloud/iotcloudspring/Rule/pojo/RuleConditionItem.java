package com.atguigu.cloud.iotcloudspring.Rule.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleConditionItem {
    private Long id;
    private Long groupId;
    private Long dataId;
    private String compareOp;
    private BigDecimal thresholdLow;
    private BigDecimal thresholdHigh;
    private BigDecimal thresholdValue;
}
