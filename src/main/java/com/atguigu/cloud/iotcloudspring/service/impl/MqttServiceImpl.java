package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttTopicConfigResponse;
import com.atguigu.cloud.iotcloudspring.mapper.MqttMapper;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttConfig;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import com.atguigu.cloud.iotcloudspring.service.MqttService;
import com.atguigu.cloud.iotcloudspring.websocket.WebSocketConfig;
import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MqttServiceImpl implements MqttService {

    @Resource
    private MqttMapper mqttMapper;

    @Resource
    private WebSocketConfig webSocketConfig;

    private MqttClient mqttClient;

    @Override
    public MqttConfigDTO getConfigByProjectId() {
        // 从数据库获取实体
        MqttConfig config = mqttMapper.selectByProjectId();

        if (config == null) {
            return null;
        }
        // 手动转换为DTO
        MqttConfigDTO dto = new MqttConfigDTO();
        dto.setId(config.getId());
        dto.setProjectId(config.getProjectId());
        dto.setBrokerAddress(config.getBrokerAddress());
        dto.setBrokerPort(config.getBrokerPort());
        dto.setClientId(config.getClientId());
        dto.setQos(config.getQos());
        dto.setKeepAlive(config.getKeepAlive());
        dto.setCleanSession(config.getCleanSession());
        dto.setCreatedAt(config.getCreatedAt());
        dto.setUpdatedAt(config.getUpdatedAt());
        return dto;
    }

    @Override
    public List<MqttTopicConfigResponse> getDeviceTopics(Integer userId, Integer projectId, Integer deviceId) {
        List<MqttTopicConfig> configs = mqttMapper.selectDeviceTopics(userId, projectId, deviceId);
        if (configs == null || configs.isEmpty()) {
            return Collections.emptyList();
        }
        List<MqttTopicConfigResponse> results = new ArrayList<>();
        for (MqttTopicConfig config : configs) {
            // 拼装格式为 user1/project2/device3/ceshi
            String fullTopic = "user" + userId
                    + "/project" + projectId
                    + "/device" + deviceId
                    + "/" + config.getTopic();
            MqttTopicConfigResponse vo = new MqttTopicConfigResponse();
            vo.setFullTopic(fullTopic);
            vo.setTopicType(config.getTopicType());
            vo.setDescription(config.getDescription());
            results.add(vo);
        }
        return results;
    }

    @Override
    public boolean saveOrUpdateDeviceTopic(MqttTopicConfigDTO dto) {
        // 先将 DTO 转换为实体对象
        MqttTopicConfig config = new MqttTopicConfig();
        config.setUserId(dto.getUserId());
        config.setProjectId(dto.getProjectId());
        config.setDeviceId(dto.getDeviceId());

        // 如果有 newTopic 表示需要更新为新主题，否则用旧的 topic
        if (dto.getNewTopic() != null && !dto.getNewTopic().isEmpty()) {
            config.setTopic(dto.getNewTopic());
        } else {
            config.setTopic(dto.getTopic());
        }

        config.setTopicType(dto.getTopicType());  // 直接赋值枚举类型（前提 DTO 和实体中的类型一致）
        config.setDescription(dto.getDescription());

        // 根据 userId, projectId, deviceId 和原始 topic 判断记录是否存在
        // 注意：如果你希望以旧的 topic 作为唯一键判断，则这里使用 dto.getTopic()
        MqttTopicConfig existing = mqttMapper.selectDeviceTopicByKey(
                dto.getUserId(), dto.getProjectId(), dto.getDeviceId(), dto.getTopic());

        int rows = 0;
        if (existing == null) {
            // 如果记录不存在，则插入新记录，传入转换后的实体对象
            rows = mqttMapper.insertDeviceTopic(config);
        } else {
            // 如果记录存在，则更新记录（可以选择更新 description、以及 topic（如果有 newTopic）等字段）
            rows = mqttMapper.updateDeviceTopicByKey(config);
        }
        return rows > 0;
    }
}
