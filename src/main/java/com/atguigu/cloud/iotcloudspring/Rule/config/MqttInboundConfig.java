package com.atguigu.cloud.iotcloudspring.Rule.config;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@Configuration
public class MqttInboundConfig {

    @Bean
    public MqttClient inboundClient(DeviceDataListener listener) throws MqttException {
        MqttClient c = new MqttClient("tcp://1.94.32.220:1883",
                "cloud-srv-" + UUID.randomUUID());
        MqttConnectOptions opt = new MqttConnectOptions();
        opt.setUserName("cloud");
        opt.setPassword("123456".toCharArray());
        c.connect(opt);

        c.subscribe("/+/+/+/+/property/#", (topic, m) -> listener.handle(topic, m));
        return c;
    }
}

@Component
@RequiredArgsConstructor
class DeviceDataListener {

    private final RuleEngine ruleEngine;
    private final ObjectMapper mapper;      // 构造器注入

    void handle(String topic, MqttMessage msg) throws IOException {
        String[] seg = topic.split("/");
        long deviceId = Long.parseLong(seg[2].substring(6));   // device9→9

        JsonNode root = mapper.readTree(msg.getPayload());
        long dataId   = root.path("dataId").asLong();
        BigDecimal v  = root.path("value").decimalValue();

        ruleEngine.evaluate(deviceId, dataId, v);
    }
}
