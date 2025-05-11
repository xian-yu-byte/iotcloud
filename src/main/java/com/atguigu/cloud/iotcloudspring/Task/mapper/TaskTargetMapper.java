package com.atguigu.cloud.iotcloudspring.Task.mapper;

import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskTargetMapper {
    int insertTarget(TaskTarget target);

    List<TaskTarget> selectByTaskId(@Param("taskId") Long taskId);
}
