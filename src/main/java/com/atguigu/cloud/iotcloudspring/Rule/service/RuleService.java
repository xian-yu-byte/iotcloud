package com.atguigu.cloud.iotcloudspring.Rule.service;

import com.atguigu.cloud.iotcloudspring.Rule.DTO.CreateRuleDTO;
import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleCardDTO;
import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleDeviceDTO;
import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleTargetsDTO;

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

    /**
     *
     * delRule
     * */

    Boolean delRule(Long ruleId);

    /**
     *
     * 根据项目id获取规则的基本信息
     * */
    List<RuleTargetsDTO> listByProject(Long projectId ,Long ruleId);

    /**
     *
     * 根据规则id获取该规则下的所有设备
     * */
    List<RuleDeviceDTO> getDevicesWithEnable(Long ruleId);
}
