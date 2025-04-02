package com.atguigu.cloud.iotcloudspring.pojo.mqtt;

import com.atguigu.cloud.iotcloudspring.entity.enums.mqtt.mqttTopicConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicConfig {
    private Integer id;
    private Integer userId;
    private Integer projectId;     // mqtt_topic.project_id
    private Integer deviceId;      // mqtt_topic.device_id
    private String topic;          // mqtt_topic.topic
    private mqttTopicConfig topicType;      // mqtt_topic.topic_type (enum) 固定主题 自定义主题
    private String description;    // mqtt_topic.description
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
