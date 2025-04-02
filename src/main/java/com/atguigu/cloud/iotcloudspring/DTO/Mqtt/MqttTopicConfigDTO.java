package com.atguigu.cloud.iotcloudspring.DTO.Mqtt;

import com.atguigu.cloud.iotcloudspring.entity.enums.mqtt.mqttTopicConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicConfigDTO {
    private Integer userId;
    private Integer projectId;
    private Integer deviceId;
    private String newTopic;
    private String topic;
    private mqttTopicConfig topicType;
    private String description;
}
