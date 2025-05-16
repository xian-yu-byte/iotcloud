package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleTargetDTO {
    private String targetType;
    private Long targetId;
}
