package com.atguigu.cloud.iotcloudspring.DTO.PythonCallBack;

import lombok.Data;

@Data
public class ChatCallBackDTO {
    private String agentId;
    private String role;     // "assistant" / "user"
    private String content;  // 文本
    private Long   timestamp;
}
