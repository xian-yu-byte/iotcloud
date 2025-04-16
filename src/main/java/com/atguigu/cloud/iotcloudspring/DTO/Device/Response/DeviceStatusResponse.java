package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatusResponse {
    private Integer id;
    private String devicestatus;
}
