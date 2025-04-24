package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.Common.service.BaseService;
import com.atguigu.cloud.iotcloudspring.pojo.Model.AiModelConfig;

public interface ModelConfigService extends BaseService<AiModelConfig> {
    /**
     * 根据ID获取模型配置
     *
     * @param id      模型ID
     * @param isCache 是否缓存
     * @return 模型配置实体
     */
    AiModelConfig getModelById(String id, boolean isCache);
}
