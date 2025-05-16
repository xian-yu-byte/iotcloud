package com.atguigu.cloud.iotcloudspring.Rule.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleTarget {
    private Long id;
    private Long ruleId;
    private String targetType;
    private Long targetId;
}
