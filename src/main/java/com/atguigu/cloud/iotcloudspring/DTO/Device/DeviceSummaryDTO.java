package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.Data;

@Data
public class DeviceSummaryDTO {
    private Long id;
    private String deviceKey;
    private String deviceName;
    private String deviceLocation;
}
