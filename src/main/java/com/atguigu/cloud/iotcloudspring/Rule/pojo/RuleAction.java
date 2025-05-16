package com.atguigu.cloud.iotcloudspring.Rule.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleAction {
    private Long id;
    private Long ruleId;
    private Integer sortOrder;
    private String actionType;
    private String actionParams;
}
