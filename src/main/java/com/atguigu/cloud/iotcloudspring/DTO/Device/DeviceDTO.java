package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.Data;

@Data
public class DeviceDTO {
    private Integer projectid;
    private Integer devicetypeid;
    private String devicename;
    private String devicekey;
    private String deviceinformation;
}
