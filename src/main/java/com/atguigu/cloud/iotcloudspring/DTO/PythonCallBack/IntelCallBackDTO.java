package com.atguigu.cloud.iotcloudspring.DTO.PythonCallBack;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class IntelCallBackDTO {
    private String projectId;
    private String agentId;
    private String deviceId;
    private String intent;   // "turn_on" / "turn_off"...
    private String value;    // 可选
    private String text;
    private long timestamp;
}
