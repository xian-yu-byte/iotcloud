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

    int updateEnabled(@Param("id") Long id, @Param("enabled") Boolean enabled);

    Task selectById(@Param("taskId") Long taskId);

    long deleteTaskById(@Param("taskId") Long taskId);

    // 通用更新：把传入对象中非空字段都更新到数据库
    int updateById(Task task);

    // 仅更新 status 字段
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
