package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionItemDTO {
    private Long dataId;
    private String compareOp;     // > = <
    private BigDecimal thresholdLow;
    private BigDecimal thresholdHigh;
    private BigDecimal thresholdValue;
}
