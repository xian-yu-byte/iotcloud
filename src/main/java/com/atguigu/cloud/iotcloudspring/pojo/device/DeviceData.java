package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceData {
    private Integer id;
    private Integer deviceid;
    private Integer devicetypeattributeid;
    private String datakey;
    private String datavalue;
    private String devicestatus;
    private LocalDateTime timestamp;
}
