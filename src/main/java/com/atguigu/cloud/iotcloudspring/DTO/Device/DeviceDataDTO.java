package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDataDTO {
    private Long deviceId;
    private String dataKey;
    private String dataValue;
    private LocalDateTime timestamp;
}
