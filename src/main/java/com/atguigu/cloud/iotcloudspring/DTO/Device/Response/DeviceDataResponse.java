package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDataResponse {
    private Integer devicetypeattributeid;
    private String datakey;
    private String datavalue;
    private String devicestatus;
    private LocalDateTime timestamp;
}
