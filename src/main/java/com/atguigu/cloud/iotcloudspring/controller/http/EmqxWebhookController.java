package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.websocket.EmqxWebhookPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmqxWebhookController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public EmqxWebhookController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/emqx/webhook")
    public ResponseEntity<String> handleEmqxWebhook(@RequestBody EmqxWebhookPayload payload) {
        // 判断事件类型，比如 "client_connected" 表示设备连接成功
        if ("client_connected".equalsIgnoreCase(payload.getEvent())) {
            String message = "设备 " + payload.getClientId() + " 连接成功";
            // 将消息推送到 WebSocket 订阅地址 /topic/connectionStatus
            messagingTemplate.convertAndSend("/topic/connectionStatus", message);
        }
        return ResponseEntity.ok("Webhook Received");
    }
}
