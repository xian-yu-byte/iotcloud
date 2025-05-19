package com.atguigu.cloud.iotcloudspring.Alarm.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("alarm_condition")
public class AlarmCondition {
    private Long id;
    private Long alarmId;
    private Integer seq;
    private String logicalOp;
    private String attributeKey;
    private String CompareOp;
    private BigDecimal thresholdValue;
    private BigDecimal thresholdLow;
    private BigDecimal thresholdHigh;
}