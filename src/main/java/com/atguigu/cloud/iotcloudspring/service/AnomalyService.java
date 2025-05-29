package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DataPointDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.InferResponseDto;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.BatchInferResponse;

import java.util.List;

public interface AnomalyService {
    /**
     * 触发对某项目的模型重训，调用 Python /train/{projectId} 接口
     */
    void train(Long projectId, List<DataPointDTO> data);

    /**
     * 调用 Python /infer/{projectId} 接口，返回异常检测结果
     */
    InferResponseDto infer(Long projectId, List<DataPointDTO> data);

    BatchInferResponse batchInfer(Long projectId,
                                  List<List<DataPointDTO>> windows);
}
