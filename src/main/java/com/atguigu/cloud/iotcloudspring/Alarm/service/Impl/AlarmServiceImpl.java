package com.atguigu.cloud.iotcloudspring.Alarm.service.Impl;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.*;
import com.atguigu.cloud.iotcloudspring.Alarm.enums.EventStatus;
import com.atguigu.cloud.iotcloudspring.Alarm.mapper.AlarmConditionMapper;
import com.atguigu.cloud.iotcloudspring.Alarm.mapper.AlarmEventMapper;
import com.atguigu.cloud.iotcloudspring.Alarm.mapper.AlarmRuleMapper;
import com.atguigu.cloud.iotcloudspring.Alarm.mapper.AlarmStateMapper;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmCondition;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmEvent;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmRule;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmState;
import com.atguigu.cloud.iotcloudspring.Alarm.service.AlarmService;
import com.atguigu.cloud.iotcloudspring.WeChat.mapper.UserOpenidMapper;
import com.atguigu.cloud.iotcloudspring.WeChat.pojo.UserOpenid;
import com.atguigu.cloud.iotcloudspring.WeChat.service.Impl.WeChatMessageService;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.mapper.ICMapper;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRuleMapper alarmRuleMapper;
    private final ICMapper icMapper;
    private final AlarmConditionMapper alarmConditionMapper;
    private final AlarmStateMapper alarmStateMapper;
    private final AlarmEventMapper alarmEventMapper;
    private final UserOpenidMapper userOpenidMapper;
    private final DeviceMapper deviceMapper;
    private final WeChatMessageService weChatMessageService;

    @Transactional
    @Override
    public Long createAlarmRule(AlarmRuleCreateDTO dto) {

        // 校验输入—— 比如 BETWEEN 一定要有上下限

        // 保存主表
        AlarmRule rule = new AlarmRule();
        BeanUtils.copyProperties(dto, rule);
        alarmRuleMapper.insert(rule);

        // 保存条件
        List<AlarmCondition> list = new ArrayList<>();
        int seq = 0;
        for (AlarmConditionDTO c : dto.getConditions()) {
            AlarmCondition entity = new AlarmCondition();
            BeanUtils.copyProperties(c, entity);
            entity.setAlarmId(rule.getId());
            entity.setSeq(seq++);
            list.add(entity);
        }
        if (!list.isEmpty()) {
            alarmConditionMapper.insertBatch(list);
        }

        return rule.getId();
    }

    @Transactional
    public void evaluateBatch(Long deviceId, Map<Long, BigDecimal> valueMap) {
        log.info("【告警】开始 evaluateBatch, deviceId={}，valueMap={}", deviceId, valueMap);

        // 取设备信息
        Device device = deviceMapper.selectDeviceById(deviceId);
        Long projectId = device.getProjectid();
        String projectName = icMapper.selectProjectNameById(projectId);
        String deviceName = device.getDevicename();
        Long deviceTypeId = device.getDevicetypeid();

        ProjectAdd project = icMapper.selectProjectById(projectId);
        if (project == null) {
            // 如果项目不存在，直接返回或记录日志
            log.warn("项目 {} 不存在！", projectId);
            return;
        }
        Long projectOwnerUserId = project.getUserid();

        // 拉取生效规则
        List<AlarmRule> rules =
                alarmRuleMapper.selectEnabledRules(projectId, deviceId, deviceTypeId);

        // 遍历每条规则
        for (AlarmRule rule : rules) {
            // 3.1 拿条件明细
            List<AlarmCondition> conds = alarmConditionMapper
                    .selectList(new QueryWrapper<AlarmCondition>()
                            .eq("alarm_id", rule.getId())
                            .orderByAsc("seq"));
            log.info("→ 规则 {} 的条件: {}", rule.getId(), conds);

            // 布尔组合
            boolean match = false;
            for (int i = 0; i < conds.size(); i++) {
                AlarmCondition c = conds.get(i);
                Long attrId;
                try {
                    attrId = Long.valueOf(c.getAttributeKey());
                } catch (NumberFormatException ex) {
                    continue;
                }

                BigDecimal actual = valueMap.get(attrId);
                boolean pass = evaluateSingle(c, actual);

                if (i == 0) {
                    match = pass;
                } else if ("AND".equalsIgnoreCase(c.getLogicalOp())) {
                    match = match && pass;
                } else {
                    match = match || pass;
                }
            }
            log.info("→ 规则 {} 的最终 match = {}", rule.getId(), match);

            AlarmState state = alarmStateMapper.selectOne(
                    new QueryWrapper<AlarmState>()
                            .eq("alarm_id", rule.getId())
                            .eq("device_id", deviceId)
            );
            if (state == null) {
                state = new AlarmState(rule.getId(), deviceId);
            }

            // 5.1 repeat_count
            if (match) {
                state.setCount(state.getCount() + 1);
                if (rule.getRepeatCount() > 0 && state.getCount() < rule.getRepeatCount()) {
                    alarmStateMapper.upsert(state);
                    continue;
                }
            } else {
                state.setCount(0);
                state.setFirstTime(null);
            }

            // 5.2 duration_sec
            if (rule.getDurationSec() > 0 && match) {
                if (state.getFirstTime() == null) {
                    state.setFirstTime(LocalDateTime.now());
                    alarmStateMapper.upsert(state);
                    continue;
                }
                long sec = Duration.between(state.getFirstTime(), LocalDateTime.now()).getSeconds();
                if (sec < rule.getDurationSec()) {
                    alarmStateMapper.upsert(state);
                    continue;
                }
            }

            // 6. 去重：如果已经是 open，则跳过；若 match=false，则做“清除”操作
//            if (Boolean.TRUE.equals(state.getIsOpen())) {
//                if (!match) {
//                    clearEvent(rule.getId(), deviceId);
//                    state.setIsOpen(false);
//                }
//                alarmStateMapper.upsert(state);
//                continue;
//            }

            // 7. 真正触发
            if (match) {
                // 7.1 写 alarm_event
                AlarmEvent ev = new AlarmEvent();
                ev.setAlarmId(rule.getId());
                ev.setDeviceId(deviceId);

                // 最后一个条件的 attrId
                AlarmCondition lastCond = conds.get(conds.size() - 1);
                Long lastAttrId = Long.valueOf(lastCond.getAttributeKey());
                ev.setAttributeKey(lastAttrId.toString()); // 存的时候用字符串形式也没问题
                ev.setCurrentValue(valueMap.get(lastAttrId));
                ev.setAlertLevel(rule.getAlertLevel());
                ev.setTriggerTime(LocalDateTime.now());
                ev.setStatus(EventStatus.OPEN);
                alarmEventMapper.insert(ev);

                // 7.2 推送给所有绑定 openid，这里需要把 attrId 转成属性名来显示
                List<UserOpenid> subs = userOpenidMapper
                        .selectList(new QueryWrapper<UserOpenid>()
                                .eq("user_id", projectOwnerUserId));
                log.info("→ 项目 {} 对应的用户 {} 的 openid 列表: {}", projectId, projectOwnerUserId, subs);
                // 查属性名：因为 attribute_key 原来存的是 ID
                // 如果你要在消息里显示哪个字段触发，可以额外查一次属性名：
                String lastAttrName = deviceMapper.selectAttributeKeyById(lastAttrId);

                for (UserOpenid u : subs) {
                    log.info("  准备向 openid={} 发送告警模板消息", u.getOpenid());
                    weChatMessageService.sendAlarmTemplate(
                            u.getOpenid(),
                            projectName,
                            deviceName,
                            lastAttrName + "超限",          // 这里可以拼成“湿度 超限”
                            deviceId.toString(),
                            ev.getTriggerTime()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            rule.getAlertLevel(),
                            ev.getId()
                    );
                    log.info("  已向 openid={} 发送告警模板消息", u.getOpenid());
                }
                state.setIsOpen(true);
            }

            // 8. 保存最新状态
            alarmStateMapper.upsert(state);
        }
    }

    /**
     * 单条件判断
     */
    private boolean evaluateSingle(AlarmCondition c, BigDecimal actual) {
        if (actual == null) {
            return false;
        }
        String op = c.getCompareOp();  // 返回“>”、“>=”等
        return switch (op) {
            case ">" -> actual.compareTo(c.getThresholdValue()) > 0;
            case ">=" -> actual.compareTo(c.getThresholdValue()) >= 0;
            case "<" -> actual.compareTo(c.getThresholdValue()) < 0;
            case "<=" -> actual.compareTo(c.getThresholdValue()) <= 0;
            case "=" -> actual.compareTo(c.getThresholdValue()) == 0;
            case "!=" -> actual.compareTo(c.getThresholdValue()) != 0;
            case "BETWEEN" -> actual.compareTo(c.getThresholdLow()) >= 0
                    && actual.compareTo(c.getThresholdHigh()) <= 0;
            case "NOT_BETWEEN" -> actual.compareTo(c.getThresholdLow()) < 0
                    || actual.compareTo(c.getThresholdHigh()) > 0;
            default -> false;
        };
    }

    /**
     * 清除已有告警
     */
    private void clearEvent(Long ruleId, Long deviceId) {
        AlarmEvent openEv = alarmEventMapper
                .selectOne(new QueryWrapper<AlarmEvent>()
                        .eq("alarm_id", ruleId)
                        .eq("device_id", deviceId)
                        .eq("status", EventStatus.OPEN));
        if (openEv != null) {
            openEv.setStatus(EventStatus.CLEARED);
            openEv.setClearedTime(LocalDateTime.now());
            alarmEventMapper.updateById(openEv);
        }
    }

    @Override
    public List<AlarmRuleShowDTO> showAlarmRule(Long projectId) {
        return alarmRuleMapper.selectAlarmRuleShowDTO(projectId);
    }

    @Override
    public AlarmRuleDetailDTO getAlarmRuleDetail(Long alarmId) {
        AlarmRuleDetailDTO rule = alarmRuleMapper.selectRuleById(alarmId);
        if (rule == null) {
            return null;
        }

        List<AlarmRuleEditDTO> condList = alarmConditionMapper.selectByAlarmId(alarmId);
        rule.setConditions(condList);

        return rule;
    }

    @Override
    public Boolean isDelAlarmRules(Long alarmId) {
        return alarmRuleMapper.deleteAlarmById(alarmId);
    }

    @Override
    public List<AlarmLogoInfoHistoryDTO> getLogoInfoHistory(Long alarmId) {
        List<AlarmLogoInfoHistoryRawDTO> rawList = alarmEventMapper.selectHistoryByAlarmId(alarmId);

        List<AlarmLogoInfoHistoryDTO> converted = rawList.stream().map(raw -> {
            AlarmLogoInfoHistoryDTO dto = new AlarmLogoInfoHistoryDTO();
            dto.setDeviceName(raw.getDeviceName());
            dto.setTriggerTime(raw.getTriggerTime());

            Long attrId = Long.valueOf(raw.getAttributeKey());
            String attrName = deviceMapper.selectAttributeKeyById(attrId);
            if (attrName == null) {
                attrName = raw.getAttributeKey().toString();
            }

            String reason;
            if ("BETWEEN".equals(raw.getCompareOp()) || "NOT_BETWEEN".equals(raw.getCompareOp())) {
                reason = String.format("%s %s %s AND %s",
                        attrName,
                        raw.getCompareOp(),
                        raw.getThresholdLow(),
                        raw.getThresholdHigh());
            } else {
                reason = String.format("%s %s %s",
                        attrName,
                        raw.getCompareOp(),
                        raw.getThresholdValue());
            }
            dto.setReason(reason);

            return dto;
        }).toList();

        Map<String, List<AlarmLogoInfoHistoryDTO>> grouped =
                converted.stream().collect(Collectors.groupingBy(
                        dto -> dto.getDeviceName() + "|" + dto.getTriggerTime().toString()
                ));

        List<AlarmLogoInfoHistoryDTO> mergedList = new ArrayList<>();
        grouped.forEach((key, listOfDtos) -> {
            String deviceName = listOfDtos.get(0).getDeviceName();
            LocalDateTime triggerTime = listOfDtos.get(0).getTriggerTime();

            if (listOfDtos.size() == 1) {
                mergedList.add(listOfDtos.get(0));
            } else {
                String combinedReason = listOfDtos.stream()
                        .map(AlarmLogoInfoHistoryDTO::getReason)
                        .collect(Collectors.joining(" 且 "));
                AlarmLogoInfoHistoryDTO mergedDto = new AlarmLogoInfoHistoryDTO();
                mergedDto.setDeviceName(deviceName);
                mergedDto.setTriggerTime(triggerTime);
                mergedDto.setReason(combinedReason);
                mergedList.add(mergedDto);
            }
        });

        // 按 triggerTime 倒序排列
        mergedList.sort(Comparator.comparing(AlarmLogoInfoHistoryDTO::getTriggerTime).reversed());

        return mergedList;
    }

    @Override
    public List<AlarmLogoInfoHistorysDTO> getFullHistoryByProjectId(Long projectId) {

        List<AlarmLogoInfoHistoryFullRawDTO> rawList =
                alarmEventMapper.selectFullHistoryByProjectId(projectId);

        List<AlarmLogoInfoHistorysDTO> converted = rawList.stream()
                .map(raw -> {
                    AlarmLogoInfoHistorysDTO dto = new AlarmLogoInfoHistorysDTO();
                    dto.setDeviceName(raw.getDeviceName());
                    dto.setAlarmName(raw.getAlarmName());
                    dto.setAlertLevel(raw.getAlertLevel());
                    dto.setTriggerTime(raw.getTriggerTime());

                    // —— 这里是关键：先查属性名称 ——
                    Long attrId = Long.valueOf(raw.getAttributeKey());
                    String attrName = deviceMapper.selectAttributeKeyById(attrId);
                    if (attrName == null) {
                        // 如果查不到，就用 ID 的字符串形式兜底
                        attrName = String.valueOf(attrId);
                    }

                    String reason;
                    if ("BETWEEN".equals(raw.getCompareOp()) || "NOT_BETWEEN".equals(raw.getCompareOp())) {
                        reason = String.format("%s %s %s AND %s",
                                attrName,                 // 这时 attrName 比如 “温度”
                                raw.getCompareOp(),       // 比如 “BETWEEN”
                                raw.getThresholdLow(),    // 范围下限
                                raw.getThresholdHigh()    // 范围上限
                        );
                    } else {
                        reason = String.format("%s %s %s",
                                attrName,                 // e.g. “温度”
                                raw.getCompareOp(),       // e.g. “>”
                                raw.getThresholdValue()   // 单值阈
                        );
                    }
                    dto.setReason(reason);

                    return dto;
                })
                .toList();

        Map<String, List<AlarmLogoInfoHistorysDTO>> grouped =
                converted.stream().collect(Collectors.groupingBy(
                        e -> e.getDeviceName() + "|" + e.getTriggerTime().toString() + "|" + e.getAlarmName()
                ));

        List<AlarmLogoInfoHistorysDTO> mergedList = new ArrayList<>();
        grouped.forEach((key, listOfDtos) -> {
            String deviceName  = listOfDtos.get(0).getDeviceName();
            String alarmName   = listOfDtos.get(0).getAlarmName();
            String alertLevel  = listOfDtos.get(0).getAlertLevel();
            LocalDateTime triggerTime = listOfDtos.get(0).getTriggerTime();

            if (listOfDtos.size() == 1) {
                mergedList.add(listOfDtos.get(0));
            } else {
                String combinedReason = listOfDtos.stream()
                        .map(AlarmLogoInfoHistorysDTO::getReason)
                        .collect(Collectors.joining(" 且 "));
                AlarmLogoInfoHistorysDTO mergedDto = new AlarmLogoInfoHistorysDTO();
                mergedDto.setDeviceName(deviceName);
                mergedDto.setAlarmName(alarmName);
                mergedDto.setAlertLevel(alertLevel);
                mergedDto.setTriggerTime(triggerTime);
                mergedDto.setReason(combinedReason);
                mergedList.add(mergedDto);
            }
        });

        mergedList.sort(Comparator.comparing(AlarmLogoInfoHistorysDTO::getTriggerTime).reversed());

        return mergedList;
    }
}
