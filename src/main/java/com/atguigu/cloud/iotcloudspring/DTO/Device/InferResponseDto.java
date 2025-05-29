package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InferResponseDto {
    private double score;
    private int label;
    private boolean abnormal;
}
