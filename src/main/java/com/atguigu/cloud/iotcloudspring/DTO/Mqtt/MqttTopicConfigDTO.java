package com.atguigu.cloud.iotcloudspring.DTO.Mqtt;

import com.atguigu.cloud.iotcloudspring.filter.enums.mqtt.mqttTopicConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicConfigDTO {
    private Long userId;
    private Long projectId;
    private Long deviceId;
    private String newTopic;
    private String topic;
    private mqttTopicConfig topicType;
    private String description;
}
