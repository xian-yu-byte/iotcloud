package com.atguigu.cloud.iotcloudspring.Alarm.service.Impl;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmConditionDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleCreateDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.mapper.AlarmConditionMapper;
import com.atguigu.cloud.iotcloudspring.Alarm.mapper.AlarmRuleMapper;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmCondition;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmRule;
import com.atguigu.cloud.iotcloudspring.Alarm.service.AlarmService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmConditionMapper alarmConditionMapper;
    private final AlarmRuleMapper alarmRuleMapper;

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

}
