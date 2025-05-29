package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCountDTO {
    private String timestamp;
    private Integer upCount;
    private Integer downCount;
}
