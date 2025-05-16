package com.atguigu.cloud.iotcloudspring.Rule.service;

import com.atguigu.cloud.iotcloudspring.Rule.DTO.CreateRuleDTO;
import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleCardDTO;

import java.util.List;

public interface RuleService {
    /**
     * 创建一条新规则，连同 target、group、item、action 一并插入
     *
     * @return 新规则的主键 ID
     */
    Long createRule(CreateRuleDTO dto);

    /**
     * 根据项目id获取卡片数据显示出来
     */
    List<RuleCardDTO> getRuleCard(Long projectId);

    /**
     * 根据规则id更新enable
     */
    Boolean updateEnableRule(Long ruleId, Boolean enable);
}
