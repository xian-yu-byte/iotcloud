package com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response;

import com.atguigu.cloud.iotcloudspring.entity.enums.mqtt.mqttTopicConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicConfigResponse {
    private String fullTopic;
    private mqttTopicConfig topicType;
    private String description;
}
