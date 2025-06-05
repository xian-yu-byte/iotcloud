package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttTopicConfigResponse;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.mapper.MqttMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import com.atguigu.cloud.iotcloudspring.service.MqttTopicConfigService;
import com.atguigu.cloud.iotcloudspring.mapper.MqttTopicConfigMapper;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cat
 * @description 针对表【mqtt_topic_config(mqtt主题表)】的数据库操作Service实现
 * @createDate 2025-05-29 16:22:04
 */
@Service
public class MqttTopicConfigServiceImpl extends ServiceImpl<MqttTopicConfigMapper, MqttTopicConfig>
        implements MqttTopicConfigService {

    @Nullable
    @Override
    public MqttTopicConfig getTopic(Long topicId) {
        return getOne(
                new LambdaQueryWrapper<MqttTopicConfig>()
                        .eq(MqttTopicConfig::getId, topicId)
        );
    }

    @Override
    public List<MqttTopicConfig> getTopics(Long userId) {
        return list(
                new LambdaQueryWrapper<MqttTopicConfig>()
                        .eq(MqttTopicConfig::getUserId, userId)
        );
    }

    @Override
    public List<MqttTopicConfig> getTopics(Long userId, Long projectId) {
        return list(
                new LambdaQueryWrapper<MqttTopicConfig>()
                        .eq(MqttTopicConfig::getUserId, userId)
                        .eq(MqttTopicConfig::getProjectId, projectId)
        );
    }

    @Override
    public List<MqttTopicConfig> getTopics(Long userId, Long projectId, Long deviceId) {
        return list(
                new LambdaQueryWrapper<MqttTopicConfig>()
                        .eq(MqttTopicConfig::getUserId, userId)
                        .eq(MqttTopicConfig::getProjectId, projectId)
                        .eq(MqttTopicConfig::getDeviceId, deviceId)
        );
    }

    @Nullable
    @Override
    public MqttTopicConfig getTopic(Long userId, Long projectId, Long deviceId, String topic) {
        return getOne(
                new LambdaQueryWrapper<MqttTopicConfig>()
                        .eq(MqttTopicConfig::getUserId, userId)
                        .eq(MqttTopicConfig::getProjectId, projectId)
                        .eq(MqttTopicConfig::getDeviceId, deviceId)
                        .eq(MqttTopicConfig::getTopic, topic)
        );
    }
    @Nullable
    @Override
    public MqttTopicConfig getTopic(String fullTopic) {

        Pattern pattern = Pattern.compile("^user(.*?)/project(.*?)/device(.*?)/(.*)$");
        Matcher matcher = pattern.matcher(fullTopic);

        Long userId = Long.parseLong(matcher.group(1));
        Long projectId = Long.parseLong(matcher.group(2));
        Long deviceId = Long.parseLong(matcher.group(3));
        String topic = matcher.group(4);

        return getTopic(userId, projectId, deviceId, topic);
    }

    @Override
    public List<MqttTopicConfig> getEffectiveTopics() {
        return list(
                new LambdaQueryWrapper<MqttTopicConfig>()
                        .eq(MqttTopicConfig::getEffective, true)
        );
    }

    @Override
    public boolean setTopicEffective(Long topicId, boolean effective) {
        return update(new LambdaUpdateWrapper<MqttTopicConfig>().set(MqttTopicConfig::getEffective, effective));
    }

    /*@Override
    public List<MqttTopicConfigResponse> getDeviceTopics(Long userId, Long projectId, Long deviceId) {
        LambdaQueryWrapper<MqttTopicConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(MqttTopicConfig::getUserId, userId);
        lqw.eq(MqttTopicConfig::getProjectId, projectId);
        lqw.eq(MqttTopicConfig::getDeviceId, deviceId);

        return list(lqw).stream().map(
                config -> new MqttTopicConfigResponse()
                        .setFullTopic(
                                "user" + userId
                                        + "/project" + projectId
                                        + "/device" + deviceId
                                        + "/" + config.getTopic()
                        )
                        .setId(config.getId())
                        .setTopicType(config.getTopicType())
                        .setAutoSubscribed(config.getAutoSubscribed())
                        .setEffective(config.getEffective())
                        .setDescription(config.getDescription())
        ).toList();

    }*/


}




