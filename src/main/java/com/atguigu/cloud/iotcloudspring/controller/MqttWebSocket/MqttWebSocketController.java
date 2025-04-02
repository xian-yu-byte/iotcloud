package com.atguigu.cloud.iotcloudspring.controller.MqttWebSocket;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttConnectResponse;
import com.atguigu.cloud.iotcloudspring.service.impl.MqttEclipsePaho;
import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MqttWebSocketController {

    @Resource
    private MqttEclipsePaho mqttEclipsePaho;

    /**
     * 前端发送连接请求（比如包含 brokerUrl, clientId, username, password 等参数）
     */
    @MessageMapping("/mqtt/connect")
    @SendTo("/topic/connectionStatus")
    public String connect(MqttConnectResponse request) {
        try {
            mqttEclipsePaho.connect(request.getBrokerUrl(), request.getClientId(),
                    request.getUsername(), request.getPassword());
            return "连接成功";
        } catch (MqttException e) {
            e.printStackTrace();
            return "连接失败：" + e.getMessage();
        }
    }
}
