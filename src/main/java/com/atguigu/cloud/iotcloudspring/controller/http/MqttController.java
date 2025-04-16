package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttTopicConfigResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.TopicRequest;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttSubscriber;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.service.MqttService;
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
    private MqttSubscriber mqttSubscriber;

    @GetMapping("/mqtt/config")
    public Result<MqttConfigDTO> getMqttConfig(@RequestParam(required = false) Integer projectId) {
        MqttConfigDTO configDto = mqttService.getConfigByProjectId();
        if (configDto != null) {
            return Result.success(configDto);
        } else {
            return Result.error("MQTT配置不存在");
        }
    }

    // 查询设备自定义主题接口
    @GetMapping("/mqtt/topic")
    public Result<List<MqttTopicConfigResponse>> getDeviceTopics(@RequestParam("userId") Integer userId,
                                                                 @RequestParam("projectId") Integer projectId,
                                                                 @RequestParam("deviceId") Integer deviceId) {
        List<MqttTopicConfigResponse> fullTopics = mqttService.getDeviceTopics(userId, projectId, deviceId);
        if (fullTopics == null || fullTopics.isEmpty()) {
            return Result.error("自定义主题未设置");
        } else {
            return Result.success(fullTopics);
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
    public Result<Void> deleteTopic(@PathVariable("topiId") Integer topicId) {
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
            mqttSubscriber.unsubscribeTopic(topic);  // 假设你在 MqttSubscriber 中实现了此方法
            return Result.success("成功取消订阅主题：" + topic);
        } catch (Exception e) {
            return Result.error("取消订阅失败：" + e.getMessage());
        }
    }
}
