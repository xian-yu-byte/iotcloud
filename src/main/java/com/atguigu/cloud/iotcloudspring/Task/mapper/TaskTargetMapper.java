package com.atguigu.cloud.iotcloudspring.Task.mapper;

import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskTargetMapper {
    int insertTarget(TaskTarget target);
}
