package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import com.atguigu.cloud.iotcloudspring.Rule.pojo.Rule;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleAction;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleConditionGroup;
import lombok.Data;

import java.util.List;

@Data
public class RuleAggregate {
    /** rule 主表 */
    private Rule rule;

    /** 这条规则配置的全部动作 */
    private List<RuleAction> actions;

    /** 这条规则的所有条件组（可选） */
    private List<RuleConditionGroup> groups;
}
