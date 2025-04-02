package com.atguigu.cloud.iotcloudspring.DTO.Mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqttConfigDTO {
    private Integer id;
    private Integer projectId;
    private String brokerAddress;
    private Integer brokerPort;
    private String clientId;
    private Integer qos;
    private Integer keepAlive;
    private Boolean cleanSession;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
