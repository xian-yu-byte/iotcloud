package com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response;

import com.atguigu.cloud.iotcloudspring.filter.enums.mqtt.mqttTopicConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicConfigResponse {
    private Long id;
    private String fullTopic;
    private mqttTopicConfig topicType;
    private String autoSubscribed;
    private String description;
}
