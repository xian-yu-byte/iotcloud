package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionGroupDTO {
    private String logicOp;       // AND/OR
    private Integer priority;
    private List<ConditionItemDTO> conditionItems;
}
