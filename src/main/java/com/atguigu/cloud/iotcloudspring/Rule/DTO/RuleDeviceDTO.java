package com.atguigu.cloud.iotcloudspring.Rule.DTO;

import lombok.Data;

@Data
public class RuleDeviceDTO {
    private Long id;
    private String devicename;
    private Boolean enabled;
}
