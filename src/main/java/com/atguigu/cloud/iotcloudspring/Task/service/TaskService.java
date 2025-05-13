package com.atguigu.cloud.iotcloudspring.Task.service;

import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.*;
import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    /**
     * 创建新任务（含目标 & 下发内容）
     */
    void createFromRequest(CreateTaskDTO dto);

    /**
     * 获取某项目下的任务列表，并计算 relativeTime
     */
    List<TaskListItemDTO> getTaskList(Long projectId);

    /**
     * 切换任务启用状态
     */
    void setEnabled(Long id, boolean enabled);

    /**
     * 查单个任务定义，用于调度时取配置
     */
    Task getById(Long taskId);

    /**
     * 列出本任务所有 Target
     */
    List<TaskTarget> listTargets(Long taskId);

    /**
     * 记录一次执行日志
     */
    void logExecution(Long taskId,
                      Long deviceId,
                      LocalDateTime plannedTime,
                      LocalDateTime executeTime,
                      long durationMs,
                      String result,
                      String message);

    /**
     * 根据任务id删除任务
     */
    boolean deleteTaskById(long id);

    boolean updateById(Task task);

    boolean updateStatus(Long id, String status);

    void updateNextRunTime(Long taskId, LocalDateTime nextRunTime);

    List<TaskDTO> selectTaskDTOById(Long id);

    void updateOperationConfig(Long id, String operationConfig);

    void updateScheduleType(Long id, String ScheduleType);

    TaskSummaryDTO selectTaskSummary(Long taskId);

    List<DayCountDTO> selectTimeCounts(Long taskId, Integer days);

    IPage<LogDTO> getExecutionLogs(Long taskId, int pageNum, int pageSize);
}
