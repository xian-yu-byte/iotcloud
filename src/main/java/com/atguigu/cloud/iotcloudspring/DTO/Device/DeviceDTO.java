package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private Long id;
    private Long projectid;
    private Long devicetypeid;
    private String devicename;
    private String mqttusername;
    private String mqttpassword;
    private String devicelocation;
    private String devicekey;
    private String deviceinformation;
    private String devicecommunication;
    private String devicestatus;
}
