package com.atguigu.cloud.iotcloudspring.pojo.mqtt;

import com.atguigu.cloud.iotcloudspring.filter.enums.mqtt.mqttTopicConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicConfig {
    private Long id;
    private Long userId;
    private Long projectId;     // mqtt_topic.project_id
    private Long deviceId;      // mqtt_topic.device_id
    private String deviceKey;
    private String topic;          // mqtt_topic.topic
    private mqttTopicConfig topicType;      // mqtt_topic.topic_type (enum) 固定主题 自定义主题
    private Boolean isOpen;
    private String autoSubscribed;
    private String description;    // mqtt_topic.description
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
