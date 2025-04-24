package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.Common.service.BaseService;
import com.atguigu.cloud.iotcloudspring.pojo.Model.AiModelConfig;

public interface ModelService extends BaseService<AiModelConfig> {
    String getModelNameById(String id);
}
