package com.atguigu.cloud.iotcloudspring.Task.mapper;

import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.DayCountDTO;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.LogDTO;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.TaskSummaryDTO;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskExecutionLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskExecutionLogMapper {

    int insertLog(TaskExecutionLog log);

    TaskSummaryDTO selectTaskSummary(@Param("taskId") Long taskId);

    List<DayCountDTO> selectTimeCounts(@Param("taskId") Long taskId,
                                       @Param("days") Integer days);

//    List<LogDTO> selectLogs(
//            @Param("taskId") Long taskId,
//            @Param("limit") Integer limit);

    IPage<LogDTO> selectLogs(Page<?> page, @Param("taskId") Long taskId);

    List<LogDTO> selectLogsS(@Param("projectId")Long projectId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    int countLogs();
}
