package com.atguigu.cloud.iotcloudspring.controller.http;

import com.alibaba.fastjson.JSON;
import com.atguigu.cloud.iotcloudspring.DTO.PythonCallBack.IntelCallBackDTO;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttPublisher;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
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

        // 测试阶段硬编码主题
        String topic = "user10/project17/device1/ctrl";

        // payload 可以继续用 dto 的内容
        String payload = new ObjectMapper()
                .writeValueAsString(dto.getValue());

        // 发布到 MQTT
        mqttPublisher.publish(topic, payload);
        log.info("已下发 MQTT 指令 → 主题：{}，内容：{}", topic, payload);

        return Result.success();
    }
}
