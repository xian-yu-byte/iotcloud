package com.atguigu.cloud.iotcloudspring.Alarm.mapper;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmLogoInfoHistoryDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmLogoInfoHistoryFullRawDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmLogoInfoHistoryRawDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmEventMapper extends BaseMapper<AlarmEvent> {

    List<AlarmLogoInfoHistoryRawDTO> selectHistoryByAlarmId(@Param("alarmId") Long alarmId);

    List<AlarmLogoInfoHistoryFullRawDTO> selectFullHistoryByProjectId(@Param("projectId") Long projectId);
}
