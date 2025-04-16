package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConnectResponse {
    private Integer id;
    private String devicename;
    private String devicestatus;
    private LocalDateTime updatetime;
}
