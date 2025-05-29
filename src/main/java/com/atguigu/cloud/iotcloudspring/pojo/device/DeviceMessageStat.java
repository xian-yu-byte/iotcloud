package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMessageStat {
    private Long deviceId;
    private Long upCount;
    private Long downCount;
    private LocalDateTime lastUpdate;
}
