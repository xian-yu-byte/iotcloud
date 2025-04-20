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
    public List<MqttTopicConfigResponse> getDeviceTopics(Long userId, Long projectId, Long deviceId) {
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
            vo.setId(config.getId());
            vo.setFullTopic(fullTopic);
            vo.setTopicType(config.getTopicType());
            vo.setDescription(config.getDescription());
            results.add(vo);
        }
        return results;
    }

    @Override
    public boolean saveOrUpdateDeviceTopic(MqttTopicConfigDTO dto) {
        // 保证 update 操作时 newTopic 不为 null：
        if (dto.getNewTopic() == null || dto.getNewTopic().trim().isEmpty()) {
            // 若 newTopic 为空，则使用原始 topic 作为新主题
            dto.setNewTopic(dto.getTopic());
        }

        // 先尝试执行 update 操作
        Long rows = mqttMapper.updateDeviceTopicByKey(dto);
        if (rows == 0) {
            // 如果更新未生效，则记录不存在，执行插入操作
            MqttTopicConfig entity = convertToEntity(dto);
            rows = mqttMapper.insertDeviceTopic(entity);
        }
        return rows > 0;
    }

    /**
     * 将 MqttTopicConfigDTO 转换为 MqttTopicConfig 实体对象。
     * <p>
     * 转换规则为：如果 newTopic 不为空，则实体的 topic 使用 newTopic，否则使用 dto 中的 topic 字段。
     *
     * @param dto 前端传入的 MqttTopicConfigDTO 对象
     * @return 转换后的 MqttTopicConfig 实体对象
     */
    private MqttTopicConfig convertToEntity(MqttTopicConfigDTO dto) {
        MqttTopicConfig config = new MqttTopicConfig();
        config.setUserId(dto.getUserId());
        config.setProjectId(dto.getProjectId());
        config.setDeviceId(dto.getDeviceId());
        // 若 newTopic 存在，则最终要保存的 topic 使用 newTopic，否则使用原始 topic
        String finalTopic = (dto.getNewTopic() != null && !dto.getNewTopic().trim().isEmpty()) ? dto.getNewTopic() : dto.getTopic();
        config.setTopic(finalTopic);
        config.setTopicType(dto.getTopicType());
        config.setDescription(dto.getDescription());
        // createdAt、updatedAt 字段可由数据库自动维护或根据业务需要设置
        return config;
    }

    @Override
    public boolean deleteDeviceTopic(Long id) {
        return mqttMapper.deleteDeviceTopicById(id) > 0;
    }
}
