package com.atguigu.cloud.iotcloudspring.Alarm.mapper;

import com.atguigu.cloud.iotcloudspring.Alarm.pojo.AlarmState;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmStateMapper extends BaseMapper<AlarmState> {
    int upsert(AlarmState state);

}
