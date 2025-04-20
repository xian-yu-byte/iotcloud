package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceData {
    private Long id;
    private Long deviceid;
    private Long devicetypeattributeid;
    private String datakey;
    private String datavalue;
    private LocalDateTime timestamp;
}
