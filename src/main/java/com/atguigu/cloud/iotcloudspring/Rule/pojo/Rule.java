package com.atguigu.cloud.iotcloudspring.Rule.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {
    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private String triggerEvent;
    private String outerLogicOp;
    private Boolean enabled;
}
