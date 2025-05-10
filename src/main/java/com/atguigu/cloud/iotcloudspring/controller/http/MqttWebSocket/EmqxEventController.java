package com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mqtt")
public class EmqxEventController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 接收 EMQX 通过 HTTP 发送过来的连接事件
     */
    @PostMapping("/connects")
    public String handleMqttConnectEvent(@RequestBody Map<String, Object> payload) {
        System.out.println("EMQX 传过来的数据：" + payload);

        // 这里可以把消息再通过 WebSocket 推送给前端
        // 假设前端订阅了 "/topic/connectionStatus"
        messagingTemplate.convertAndSend("/topic/connectionStatus", "连接事件：" + payload);

        return "OK"; // 返回给 EMQX 的响应
    }
}
