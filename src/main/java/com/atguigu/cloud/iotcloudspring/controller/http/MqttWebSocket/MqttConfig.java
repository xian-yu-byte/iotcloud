package com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient c = new MqttClient("tcp://1.94.32.220:1883",
                "spring-pub", new MemoryPersistence());
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setUserName("cloud");
        opts.setPassword("123456".toCharArray());
        opts.setCleanSession(false);
        opts.setAutomaticReconnect(true);
        c.connect(opts);
        return c;
    }
}