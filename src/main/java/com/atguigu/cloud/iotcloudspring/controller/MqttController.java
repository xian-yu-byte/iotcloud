package com.atguigu.cloud.iotcloudspring.controller;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.Response.MqttTopicConfigResponse;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import com.atguigu.cloud.iotcloudspring.service.MqttService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class MqttController {
    @Resource
    private MqttService mqttService;

    @GetMapping("/mqtt/config")
    public Result<MqttConfigDTO> getMqttConfig(@RequestParam(required = false ) Integer projectId) {
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
            return Result.success("保存成功");
        } else {
            return Result.error("保存失败");
        }
    }
}
