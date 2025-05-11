package com.atguigu.cloud.iotcloudspring.Task.mapper;

import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.TaskListItemDTO;
import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {
    /**
     * 插入 task
     */
    int insertTask(Task task);

    List<TaskListItemDTO> selectTaskListByProject(@Param("projectId") Long projectId);
}
