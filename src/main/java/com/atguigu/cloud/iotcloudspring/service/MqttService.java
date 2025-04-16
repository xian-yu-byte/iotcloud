package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttTopicConfigResponse;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MqttService {

    MqttConfigDTO getConfigByProjectId();

    List<MqttTopicConfigResponse> getDeviceTopics(Integer userId, Integer projectId, Integer deviceId);

    //保存或更新用户自定义设备主题（Upsert）
    boolean saveOrUpdateDeviceTopic(MqttTopicConfigDTO dto);

    //删除设备主题
    boolean deleteDeviceTopic(Integer id);
}
