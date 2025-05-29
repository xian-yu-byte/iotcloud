package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Ai.DeviceIdDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttTopicConfigResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.TopicRequest;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttSubscriber;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.atguigu.cloud.iotcloudspring.service.ICSpringService;
import com.atguigu.cloud.iotcloudspring.service.MqttService;
import com.atguigu.cloud.iotcloudspring.service.MqttTopicConfigService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class MqttController {
    @Resource
    private MqttService mqttService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private MqttSubscriber mqttSubscriber;

    @Resource
    private ICSpringService icSpringService;

    @Resource
    private MqttTopicConfigService mqttTopicConfigService;

    @GetMapping("/mqtt/config")
    public Result<MqttConfigDTO> getMqttConfig(@RequestParam(required = false) Long deviceid) {
        MqttConfigDTO configDto = mqttService.getConfigByProjectId(deviceid);
        if (configDto != null) {
            return Result.success(configDto);
        } else {
            return Result.error("MQTT配置不存在");
        }
    }

    // 查询设备自定义主题接口
    @GetMapping("/mqtt/topic")
    public Result<List<MqttTopicConfigResponse>> getDeviceTopics(@RequestParam("userId") Long userId,
                                                                 @RequestParam("projectId") Long projectId,
                                                                 @RequestParam("deviceId") Long deviceId) {
        List<MqttTopicConfigResponse> fullTopics = mqttService.getDeviceTopics(userId, projectId, deviceId);
        if (fullTopics == null || fullTopics.isEmpty()) {
            return Result.error("自定义主题未设置");
        } else {
            return Result.success(fullTopics);
        }
    }

    // 自动插入并订阅主题
    @PostMapping("/createAndSubscribe")
    public Result<String> createAndSubscribe(@RequestBody DeviceIdDTO dto) {
        try {
            // 通过 DTO 拿 deviceId
            Long deviceId = dto.getDeviceId();

            // 只负责组装 topic 字符串
            String topic = deviceService.createTopic(deviceId);

            // 拿到 topic 后立即订阅
            mqttSubscriber.subscribeTopic(topic);

            return Result.success("成功保存并订阅: " + topic);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 用户自定义主题保存/更新接口
    @PostMapping("/mqtt/custom")
    public Result<String> saveOrUpdateDeviceTopic(@RequestBody MqttTopicConfigDTO dto) {
        boolean success = mqttService.saveOrUpdateDeviceTopic(dto);
        if (success) {
            // 构造完整的主题字符串，格式为 user{userId}/project{projectId}/device{deviceId}/{topic}
            String fullTopic = "user" + dto.getUserId()
                    + "/project" + dto.getProjectId()
                    + "/device" + dto.getDeviceId()
                    + "/" + dto.getTopic();
            // 保存成功后，让后端自动订阅该主题
//            mqttSubscriber.subscribeTopic(fullTopic);
            return Result.success("保存成功，并已订阅主题：" + fullTopic);
        } else {
            return Result.error("保存失败");
        }
    }

    // 删除主题
    @DeleteMapping("/deleteTopic/{topiId}")
    public Result<Void> deleteTopic(@PathVariable("topiId") Long topicId) {
        boolean success = mqttService.deleteDeviceTopic(topicId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }

    }

    // 订阅主题
    @PostMapping("/mqtt/subscribe")
    public Result<String> subscribeTopic(@RequestBody TopicRequest request) {
        try {
            String topic = request.getTopic();
            MqttTopicConfig topicConfig = mqttTopicConfigService.getTopic(topic);

            if (topicConfig == null) {
                return Result.error("订阅失败：未存在的主题！");
            }

            mqttTopicConfigService.setTopicEffective(topicConfig.getId(), true);

            mqttSubscriber.subscribeTopic(topic);
            return Result.success("成功订阅主题：" + topic);
        } catch (Exception e) {
            return Result.error("订阅失败：" + e.getMessage());
        }
    }

    // 取消订阅主题
    @PostMapping("/mqtt/unsubscribe")
    public Result<String> unsubscribeTopic(@RequestBody TopicRequest request) {
        try {
            String topic = request.getTopic();

            MqttTopicConfig topicConfig = mqttTopicConfigService.getTopic(topic);

            if (topicConfig == null) {
                return Result.error("订阅失败：未存在的主题！");
            }

            mqttTopicConfigService.setTopicEffective(topicConfig.getId(), false);

            mqttSubscriber.unsubscribeTopic(topic);
            return Result.success("成功取消订阅主题：" + topic);
        } catch (Exception e) {
            return Result.error("取消订阅失败：" + e.getMessage());
        }
    }
}
