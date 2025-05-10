package com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MqttPublisher {

    private final MqttClient client;

    public MqttPublisher() throws MqttException {
//        String brokerUrl = "ssl://vaa0511b.ala.cn-hangzhou.emqxsl.cn:8883";
        String brokerUrl = "tcp://1.94.32.220:1883";
        client = new MqttClient(brokerUrl, MqttClient.generateClientId(), new MemoryPersistence());
        MqttConnectOptions opt = new MqttConnectOptions();
        opt.setUserName("cloud");
        opt.setPassword("123456".toCharArray());
        client.connect(opt);
    }

    public void publish(String topic, String payload) {
        try {
            MqttMessage msg = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
            msg.setQos(1);
            client.publish(topic, msg);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
