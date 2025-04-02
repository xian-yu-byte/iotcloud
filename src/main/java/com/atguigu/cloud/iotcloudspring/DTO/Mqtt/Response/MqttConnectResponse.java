package com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttConnectResponse {
    private String brokerUrl;  // 例如 "tcp://brokerAddress:brokerPort"
    private String clientId;
    private String username;   // 来自 Device 表
    private String password;   // 来自 Device 表
}
