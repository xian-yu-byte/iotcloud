package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgentTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AgentTemplateService extends IService<AiAgentTemplate> {
    /**
     * 获取默认模板
     *
     * @return 默认模板实体
     */
    AiAgentTemplate getDefaultTemplate();
}
