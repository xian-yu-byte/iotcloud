package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private Long id;
    private Long projectid;
    private Long devicetypeid;
    private String devicename;
    private String devicekey;
    private String devicelocation;
    private String devicecommunication;
    private String mqttusername;
    private String mqttpassword;
    private String devicegroup;
    private String deviceinformation;
    private String devicestatus;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
}
