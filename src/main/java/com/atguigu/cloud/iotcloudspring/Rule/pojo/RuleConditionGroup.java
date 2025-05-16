package com.atguigu.cloud.iotcloudspring.Rule.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleConditionGroup {
    private Long id;
    private Long ruleId;
    private String logicOp;
    private Integer priority;
}
