package com.atguigu.cloud.iotcloudspring.Alarm.service;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleCreateDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleDetailDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleShowDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AlarmService {
    Long createAlarmRule(AlarmRuleCreateDTO dto);

    void evaluateBatch(Long deviceId, Map<Long, BigDecimal> valueMap);

    List<AlarmRuleShowDTO> showAlarmRule(Long projectId);

    AlarmRuleDetailDTO getAlarmRuleDetail(Long alarmId);

    Boolean isDelAlarmRules(Long alarmId);
}
