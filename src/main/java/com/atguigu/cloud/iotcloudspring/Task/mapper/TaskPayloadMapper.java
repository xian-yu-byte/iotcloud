package com.atguigu.cloud.iotcloudspring.Task.mapper;

import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskPayload;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskPayloadMapper {
    int insertPayload(TaskPayload payload);
}
