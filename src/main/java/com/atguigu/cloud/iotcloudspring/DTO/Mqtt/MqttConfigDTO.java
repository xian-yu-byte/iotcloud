package com.atguigu.cloud.iotcloudspring.DTO.Mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqttConfigDTO {
    private Long id;
    private Long projectId;
    private String brokerAddress;
    private Long brokerPort;
    private String clientId;
    private Long qos;
    private Long keepAlive;
    private Boolean cleanSession;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
