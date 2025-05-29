package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchInferResponse {
    /** 模型返回的 N 个打分 */
    private List<Double> scores;
    /** N 个布尔标记，true 表示异常 */
    private List<Boolean> abnormals;
}
