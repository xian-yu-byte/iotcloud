package com.atguigu.cloud.iotcloudspring.DTO.Mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicRequest {
    private String topic;
    private Boolean isOpen;
}
