package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponse {
    private  Integer id;
    private Integer projectid;
    private Integer devicetypeid;
    private String devicename;
    private String devicekey;
    private String deviceinformation;
    private String devicecommunication;
}
