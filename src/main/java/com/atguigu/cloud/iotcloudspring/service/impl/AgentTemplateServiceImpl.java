package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.mapper.AiTemplateMapper;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgentTemplate;
import com.atguigu.cloud.iotcloudspring.service.AgentTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AgentTemplateServiceImpl extends ServiceImpl<AiTemplateMapper, AiAgentTemplate> implements AgentTemplateService {

    /**
     * 获取默认模板
     *
     * @return 默认模板实体
     */
    public AiAgentTemplate getDefaultTemplate() {
        LambdaQueryWrapper<AiAgentTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AiAgentTemplate::getSort)
                .last("LIMIT 1");
        return this.getOne(wrapper);
    }
}
