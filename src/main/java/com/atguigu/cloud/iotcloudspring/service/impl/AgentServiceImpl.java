package com.atguigu.cloud.iotcloudspring.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisKeys;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisUtils;
import com.atguigu.cloud.iotcloudspring.Common.service.CodeService;
import com.atguigu.cloud.iotcloudspring.Common.service.SortService;
import com.atguigu.cloud.iotcloudspring.Common.service.impl.BaseServiceImpl;
import com.atguigu.cloud.iotcloudspring.Common.utils.ConvertUtils;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AgentDTO;
import com.atguigu.cloud.iotcloudspring.mapper.AiMapper;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgent;
import com.atguigu.cloud.iotcloudspring.service.AgentService;
import com.atguigu.cloud.iotcloudspring.service.ModelService;
import com.atguigu.cloud.iotcloudspring.service.TimbreService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl extends BaseServiceImpl<AiMapper, AiAgent> implements AgentService {
    @Autowired
    @Qualifier("aiMapper")
    private AiMapper aiMapper;
    @Resource
    private ModelService modelService;
    @Resource
    private TimbreService timbreService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private Snowflake snowflake;
    @Autowired
    private CodeService codeService;
    @Autowired
    private SortService sortService;

    @Override
    public List<AgentDTO> getAgentsByProjectId(Long projectId) {
        QueryWrapper<AiAgent> wrapper = new QueryWrapper<>();
        wrapper.eq("project_id", projectId);
        List<AiAgent> agents = aiMapper.selectList(wrapper);

        return agents.stream()
                .map(agent -> {
                    AgentDTO dto = new AgentDTO();
                    dto.setId(agent.getId());
                    dto.setAgentName(agent.getAgentName());
                    dto.setSystemPrompt(agent.getSystemPrompt());
                    dto.setTtsModelName(modelService.getModelNameById(agent.getTtsModelId()));
                    dto.setLlmModelName(modelService.getModelNameById(agent.getLlmModelId()));
                    dto.setTtsVoiceName(timbreService.getTimbreNameById(agent.getTtsVoiceId()));
                    dto.setDeviceCount(getDeviceCountByAgentId(agent.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getDeviceCountByAgentId(String agentId) {
        if (StringUtils.isBlank(agentId)) {
            return 0;
        }

        // 先从Redis中获取
        Integer cachedCount = redisUtils.get(
                RedisKeys.getAgentDeviceCountById(agentId),
                Integer.class
        );
        if (cachedCount != null) {
            return cachedCount;
        }

        // 如果Redis中没有，则从数据库查询
        Integer deviceCount = aiMapper.getDeviceCountByAgentId(agentId);

        // 将结果存入Redis
        if (deviceCount != null) {
            redisUtils.set(RedisKeys.getAgentDeviceCountById(agentId), deviceCount, 60);
        }

        return deviceCount != null ? deviceCount : 0;
    }

    @Override
    public void createAgent(AiAgent agent) {
        AiAgent entity = ConvertUtils.sourceToTarget(agent, AiAgent.class);

        entity.setId(snowflake.nextIdStr());
        // 生成智能体编码和排序号
        entity.setAgentCode(codeService.nextCode());
        entity.setSort(sortService.nextSort());
        aiMapper.insert(entity);
    }

    @Override
    public AiAgent getAgentById(String id) {
        return aiMapper.selectById(id);
    }
}
