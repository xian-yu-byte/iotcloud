package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private Integer projectid;
    private Integer devicetypeid;
    private String devicename;
    private String devicekey;
    private String deviceinformation;
    private String devicecommunication;
}
