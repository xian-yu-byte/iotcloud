package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMessageLog {
    private Long id;
    private Long deviceId;
    private String direction;
    private String topic;
    private String payload;
    private Long latencyMs;
    private LocalDateTime createdAt;
}
