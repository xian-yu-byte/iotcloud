package com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response;

import com.atguigu.cloud.iotcloudspring.filter.enums.mqtt.MqttTopicConfigType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicConfigResponse {
    private Long id;
    private String fullTopic;
    private MqttTopicConfigType topicType;
    private Integer autoSubscribed;
    private Integer effective;
    private String description;
}
