package com.atguigu.cloud.iotcloudspring.Task.mapper;

import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskExecutionLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskExecutionLogMapper {

    int insertLog(TaskExecutionLog log);
}
