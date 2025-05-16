package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionDTO {
    private Integer sortOrder;
    private String actionType;
    private Long functionId;
    private Long userId;
    private Long projectId;
    private Long targetDeviceId;
    private Object payload;
}
