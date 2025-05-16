package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRuleDTO {
    private Long projectId;
    private String name;
    private String description;
    private String triggerEvent;
    private String outerLogicOp;
    private Boolean enabled;
    private List<RuleTargetDTO> targets;
    private List<ConditionGroupDTO> conditionGroups;
    private List<RuleActionDTO> actions;
}
