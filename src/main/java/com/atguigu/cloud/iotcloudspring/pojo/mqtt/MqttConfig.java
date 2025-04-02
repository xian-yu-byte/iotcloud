package com.atguigu.cloud.iotcloudspring.pojo.mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttConfig {
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
