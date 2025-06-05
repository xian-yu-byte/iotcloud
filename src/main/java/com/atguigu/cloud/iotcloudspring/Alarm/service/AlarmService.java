package com.atguigu.cloud.iotcloudspring.Alarm.service;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AlarmService {
    Long createAlarmRule(AlarmRuleCreateDTO dto);

    void evaluateBatch(Long deviceId, Map<Long, BigDecimal> valueMap);

    List<AlarmRuleShowDTO> showAlarmRule(Long projectId);

    AlarmRuleDetailDTO getAlarmRuleDetail(Long alarmId);

    Boolean isDelAlarmRules(Long alarmId);

    List<AlarmLogoInfoHistoryDTO> getLogoInfoHistory(Long alarmId);

    List<AlarmLogoInfoHistorysDTO> getFullHistoryByProjectId(Long projectId);
}
