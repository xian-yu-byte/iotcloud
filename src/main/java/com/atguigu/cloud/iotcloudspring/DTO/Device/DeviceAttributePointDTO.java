package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAttributePointDTO {
    private LocalDateTime timestamp;
    private Double datavalue;
    private String datakey;
    private Double anomalyScore;   // IsolationForest 的 score
    private Boolean abnormal;      // true=异常
}
