package com.atguigu.cloud.iotcloudspring.controller.http;

import com.alibaba.fastjson.JSON;
import com.atguigu.cloud.iotcloudspring.DTO.PythonCallBack.IntelCallBackDTO;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttPublisher;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/callback")
@CrossOrigin("*")
@AllArgsConstructor
public class ChatCallBackController {
    private static final Logger log = LoggerFactory.getLogger(ChatCallBackController.class);

    private final MqttPublisher mqttPublisher;
    private final DeviceService deviceService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /* ---------- 聊天回调 ---------- */
//    @PostMapping("/chat")
//    public Result<Void> saveChat(@RequestBody ChatCallBackDTO dto) {
//        chatMessageMapper.insert(dto.toEntity());   // 或用 Service 封装
//        return Result.success();
//    }

    /* ---------- 设备意图回调 ---------- */
    @PostMapping("/device")
    public Result<Void> handleIntent(@RequestBody IntelCallBackDTO dto) throws JsonProcessingException {
        log.info("收到设备意图：{}", JSON.toJSONString(dto));

        // 如果是 get，直接返回成功，不下发
        ObjectMapper mapper = new ObjectMapper();
        JsonNode args = mapper.readTree(dto.getValue());
        String action = args.path("action").asText("");
        if ("get".equalsIgnoreCase(action)) {
            log.info("检测到 GET 操作，跳过下发");
            return Result.success();
        }

        // 根据 deviceKey 查到 Device + Project
        Device device = deviceService.getByKey(dto.getDeviceKey());
        if (device == null) {
            log.error("未找到设备，deviceKey={}", dto.getDeviceKey());
            return Result.error("设备不存在");
        }
        ProjectAdd project = deviceService.getById(device.getProjectid());
        if (project == null) {
            log.error("未找到项目，projectId={}", device.getProjectid());
            return Result.error("项目不存在");
        }

        // 只拼装 topic 字符串
        String topic = String.format(
                "user%s/project%s/device%s/%s",
                project.getUserid(),
                device.getProjectid(),
                device.getId(),
                dto.getDeviceKey()     // 用回 Python 传过来的 deviceKey
        );

        // 下发 payload
        String payload = mapper.writeValueAsString(dto.getValue());
        try {
            mqttPublisher.publish(topic, payload);
            log.info("已下发 MQTT 指令 → 主题：{}，内容：{}", topic, payload);
        } catch (Exception e) {
            log.error("下发 MQTT 失败，topic={}，payload={}", topic, payload, e);
            return Result.error("下发失败：" + e.getMessage());
        }

        return Result.success();
    }
}
