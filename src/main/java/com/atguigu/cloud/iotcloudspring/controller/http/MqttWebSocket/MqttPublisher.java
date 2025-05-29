package com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket;

import com.atguigu.cloud.iotcloudspring.mapper.MqttMapper;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceMessageLog;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Slf4j
public class MqttPublisher {

    private final MqttClient client;
    @Resource
    private MqttMapper mqttMapper;

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

            // 解析 deviceId
            Long deviceId = parseDeviceIdFromTopic(topic);
            if (deviceId == null) {
                log.warn("无法从主题 {} 解析出 deviceId，跳过下行统计", topic);
                return;
            }

            // 下行消息日志 & 汇总
            // 插入日志
            DeviceMessageLog downLog = new DeviceMessageLog();
            downLog.setDeviceId(parseDeviceIdFromTopic(topic));
            downLog.setDirection("DOWN");
            downLog.setTopic(topic);
            downLog.setPayload(payload);
            downLog.setCreatedAt(LocalDateTime.now());
            mqttMapper.insertMessageLog(downLog);

            // 更新汇总
            mqttMapper.upsertMessageStat(downLog.getDeviceId(), 0, 1);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    private Long parseDeviceIdFromTopic(String topic) {
        if (topic == null) return null;
        String[] parts = topic.split("/");
        for (String part : parts) {
            if (part.startsWith("device")) {
                try {
                    return Long.parseLong(part.substring("device".length()));
                } catch (NumberFormatException e) {
                    // 打日志或忽略
                    return null;
                }
            }
        }
        return null;
    }
}
