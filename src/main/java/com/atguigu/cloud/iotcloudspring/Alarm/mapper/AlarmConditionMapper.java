package com.atguigu.cloud.iotcloudspring.Alarm.mapper;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleEditDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmConditionMapper extends BaseMapper<AlarmCondition> {
    /**
     * 批量插入条件
     */
    int insertBatch(@Param("list") List<AlarmCondition> list);

    /**
     * 查询某条告警规则下所有的条件，按 seq 升序排列
     */
    List<AlarmRuleEditDTO> selectByAlarmId(@Param("alarmId") Long alarmId);
}
