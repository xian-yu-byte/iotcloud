package com.atguigu.cloud.iotcloudspring.Alarm.mapper;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleDetailDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleShowDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmRuleMapper extends BaseMapper<AlarmRule> {
    List<AlarmRule> selectEnabledRules(@Param("projectId")   Long projectId,
                                       @Param("deviceId")    Long deviceId,
                                       @Param("deviceTypeId")Long deviceTypeId);

    // 返回当前项目下的所有规则
    List<AlarmRuleShowDTO> selectAlarmRuleShowDTO(@Param("projectId") Long projectId);

    // 查询单条告警规则（不包含条件），映射到 AlarmRuleDetailDTO 的主表部分
    AlarmRuleDetailDTO selectRuleById(@Param("alarmId") Long alarmId);

    // 根据alarmId删除当前告警规则
    Boolean deleteAlarmById(@Param("alarmId") Long alarmId);
}
