package com.atguigu.cloud.iotcloudspring.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmqxWebhookPayload {
    private String event;
    private String clientId;
}
