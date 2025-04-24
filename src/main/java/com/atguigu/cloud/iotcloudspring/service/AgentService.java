package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.Common.service.BaseService;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AgentCreateDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AgentDTO;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgent;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgentTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AgentService extends BaseService<AiAgent> {

    /**
     * 获取用户智能体列表
     *
     * @param projectId
     * @return
     */
    List<AgentDTO> getAgentsByProjectId(Long projectId);

    /**
     * 获取智能体的设备数量
     *
     * @param agentId 智能体ID
     * @return 设备数量
     */
    Integer getDeviceCountByAgentId(String agentId);

    /**
     * 自动生成智能体的uuid
     *
     */
    void createAgent(AiAgent agent);

    /**
     * 获取智能体详情
     */
    AiAgent getAgentById(String id);


}
