package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttTopicConfigResponse;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author cat
 * @description 针对表【mqtt_topic_config(mqtt主题表)】的数据库操作Service
 * @createDate 2025-05-29 16:22:04
 */
public interface MqttTopicConfigService extends IService<MqttTopicConfig> {

    @Nullable
    MqttTopicConfig getTopic(Long topicId);

    List<MqttTopicConfig> getTopics(Long userId);

    List<MqttTopicConfig> getTopics(Long userId, Long projectId);

    List<MqttTopicConfig> getTopics(Long userId, Long projectId, Long deviceId);

    @Nullable
    MqttTopicConfig getTopic(Long userId, Long projectId, Long deviceId, String topic);

    @Nullable
    MqttTopicConfig getTopic(String fullTopic);

    List<MqttTopicConfig> getEffectiveTopics();

    boolean setTopicEffective(Long topicId, boolean effective);

}
