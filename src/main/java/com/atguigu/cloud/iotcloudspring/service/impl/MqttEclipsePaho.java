package com.atguigu.cloud.iotcloudspring.service.impl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

@Service
public class MqttEclipsePaho {

    private MqttClient mqttClient;

    /**
     * 连接到 MQTT Broker
     *
     * @param brokerUrl Broker 地址，例如 "tcp://broker.emqx.io:1883"
     * @param clientId  客户端ID（每个设备一个唯一的clientId）
     * @param username  设备的 MQTT 用户名
     * @param password  设备的 MQTT 密码
     * @throws MqttException 连接异常
     */
    public void connect(String brokerUrl, String clientId, String username, String password) throws MqttException {
        // 使用内存持久化~
        mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        // 可设置其他连接参数，如 keepAlive 等~
        mqttClient.connect(options);
    }

    public boolean isConnected() {
        return mqttClient != null && mqttClient.isConnected();
    }
}
