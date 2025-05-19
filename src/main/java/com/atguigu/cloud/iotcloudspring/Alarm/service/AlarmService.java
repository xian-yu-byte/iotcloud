package com.atguigu.cloud.iotcloudspring.Alarm.service;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleCreateDTO;

public interface AlarmService {
    Long createAlarmRule(AlarmRuleCreateDTO dto);
}
