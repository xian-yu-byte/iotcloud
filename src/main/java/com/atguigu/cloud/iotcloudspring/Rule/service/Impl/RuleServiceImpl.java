package com.atguigu.cloud.iotcloudspring.Rule.service.Impl;

import com.atguigu.cloud.iotcloudspring.Rule.DTO.*;
import com.atguigu.cloud.iotcloudspring.Rule.mapper.*;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.*;
import com.atguigu.cloud.iotcloudspring.Rule.service.RuleService;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {
    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private DeviceService deviceService;
    @Resource
    private RuleTargetMapper ruleTargetMapper;
    @Resource
    private ConditionGroupMapper conditionGroupMapper;
    @Resource
    private ConditionItemMapper conditionItemMapper;
    @Resource
    private RuleActionMapper ruleActionMapper;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRule(CreateRuleDTO dto) {
        // 插入主表 rule
        Rule rule = new Rule();
        BeanUtils.copyProperties(dto, rule);
        ruleMapper.insert(rule);
        Long ruleId = rule.getId();

        // 插入 rule_target
        for (RuleTargetDTO t : dto.getTargets()) {
            RuleTarget rt = new RuleTarget();
            rt.setRuleId(ruleId);
            rt.setTargetType(t.getTargetType());
            rt.setTargetId(t.getTargetId());
            ruleTargetMapper.insert(rt);
        }

        // 插入 rule_condition_group + rule_condition_item
        if (dto.getConditionGroups() == null || dto.getConditionGroups().isEmpty()) {
            throw new IllegalArgumentException("至少要有一个条件分组");
        }
        for (ConditionGroupDTO g : dto.getConditionGroups()) {
            if (g.getConditionItems() == null || g.getConditionItems().isEmpty()) {
                throw new IllegalArgumentException("条件分组里的条件项不能为空");
            }
            RuleConditionGroup group = new RuleConditionGroup();
            group.setRuleId(ruleId);
            group.setLogicOp(g.getLogicOp());
            group.setPriority(g.getPriority());
            conditionGroupMapper.insert(group);
            Long groupId = group.getId();

            for (ConditionItemDTO itemDto : g.getConditionItems()) {
                RuleConditionItem item = new RuleConditionItem();
                item.setGroupId(groupId);
                BeanUtils.copyProperties(itemDto, item);
                conditionItemMapper.insert(item);
            }
        }

        // 插入 rule_action
        for (RuleActionDTO dtoAct : dto.getActions()) {

            RuleAction act = new RuleAction();
            act.setRuleId(ruleId);
            act.setSortOrder(dtoAct.getSortOrder());
            act.setActionType(dtoAct.getActionType());

            ObjectNode params = objectMapper.createObjectNode();
            // 仅当动作类型需要下发命令 / 云函数时才拼 topic
            if ("云函数编辑".equals(dtoAct.getActionType())
                    || "DEVICE_CMD".equals(dtoAct.getActionType())) {

                // 1. 取设备 key
                String dev = deviceService.getDeviceKeyById(dtoAct.getTargetDeviceId());
                if (dev == null) {
                    throw new IllegalArgumentException("目标设备不存在，id=" + dtoAct.getTargetDeviceId());
                }

                // 2. 组装完整下行主题
                String topic = String.format(
                        "user%d/project%d/device%d/%s",
                        dtoAct.getUserId(),
                        dtoAct.getProjectId(),
                        dtoAct.getTargetDeviceId(),
                        dev);         // 数据库查

                // 3. 组装 JSON → 写入 action_params

                params.put("topic", topic);
                params.put("functionId", dtoAct.getFunctionId());          // 云函数 ID
                params.set("payload", objectMapper.valueToTree(dtoAct.getPayload()));

                act.setActionParams(params.toString());

            }
            // 其他动作类型示例
//            else if ("发送告警".equals(dtoAct.getActionType())) {
//                // 告警级别之类，自行封装
//                params.put("level", dtoAct.getPayload().toString());
//            }

            /* —— 最终写入表 —— */
            act.setActionParams(params.toString());
            ruleActionMapper.insert(act);
        }

        return ruleId;
    }

    public List<RuleCardDTO> getRuleCard(Long projectId) {
        return ruleMapper.getRuleCard(projectId);
    }

    public Boolean updateEnableRule(Long ruleId, Boolean enabled) {
        return ruleMapper.updateEnableRule(ruleId, enabled);
    }
}
