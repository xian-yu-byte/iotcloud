package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageLatencyDTO {
    private String timestamp;
    private Long avgLatencyMs;
    private Long maxLatencyMs;
    private Long minLatencyMs;
}
