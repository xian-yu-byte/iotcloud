package com.atguigu.cloud.iotcloudspring.Task.service.Impl;

import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.*;
import com.atguigu.cloud.iotcloudspring.Task.mapper.TaskExecutionLogMapper;
import com.atguigu.cloud.iotcloudspring.Task.mapper.TaskMapper;
import com.atguigu.cloud.iotcloudspring.Task.mapper.TaskPayloadMapper;
import com.atguigu.cloud.iotcloudspring.Task.mapper.TaskTargetMapper;
import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskExecutionLog;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskPayload;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;
import com.atguigu.cloud.iotcloudspring.Task.service.TaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskTargetMapper targetMapper;
    @Resource
    private TaskPayloadMapper payloadMapper;
    @Resource
    private TaskExecutionLogMapper taskExecutionLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFromRequest(CreateTaskDTO dto) {
        // 插入 task 主表
        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        taskMapper.insertTask(task);

        Long taskId = task.getId();
        Long projectId = dto.getProjectId();

        // 按设备生成 task_target
        if (dto.getDeviceIds() != null) {
            dto.getDeviceIds().forEach(deviceId -> {
                TaskTarget t = new TaskTarget();
                t.setTaskId(taskId);
                t.setProjectId(projectId);
                t.setTargetType("DEVICE");
                t.setTargetId(deviceId);
                targetMapper.insertTarget(t);
            });
        }

        // 按设备类型生成 task_target
        if (dto.getDeviceTypeIds() != null) {
            dto.getDeviceTypeIds().forEach(typeId -> {
                TaskTarget t = new TaskTarget();
                t.setTaskId(taskId);
                t.setProjectId(projectId);
                t.setTargetType("TYPE");
                t.setTargetId(typeId);
                targetMapper.insertTarget(t);
            });
        }

        // 下发内容插入 task_payload
        TaskPayload p = new TaskPayload();
        p.setTaskId(taskId);
        p.setProjectId(projectId);
        p.setParamKey("config");
        p.setParamValue(dto.getOperationConfig());
        payloadMapper.insertPayload(p);
    }

    @Override
    public List<TaskListItemDTO> getTaskList(Long projectId) {
        List<TaskListItemDTO> list = taskMapper.selectTaskListByProject(projectId);
        LocalDateTime now = LocalDateTime.now();

        for (TaskListItemDTO item : list) {
            LocalDateTime ref = item.getNextRunTime() != null
                    ? item.getNextRunTime()
                    : item.getCreatedTime();

            String rel;
            if (ref.isAfter(now)) {
                // 将来执行：xx小时后 / xx分钟后 / 即将运行
                Duration d = Duration.between(now, ref);
                long hours = d.toHours();
                if (hours > 0) {
                    rel = hours + "小时后";
                } else {
                    long mins = d.toMinutes();
                    rel = mins > 0 ? mins + "分钟后" : "即将运行";
                }
            } else {
                // 已经执行或关闭后：xx小时前 / xx分钟前 / 刚刚
                Duration d = Duration.between(ref, now);
                long hours = d.toHours();
                if (hours > 0) {
                    rel = hours + "小时前";
                } else {
                    long mins = d.toMinutes();
                    rel = mins > 0 ? mins + "分钟前" : "刚刚";
                }
            }
            item.setRelativeTime(rel);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setEnabled(Long id, boolean enabled) {
        taskMapper.updateEnabled(id, enabled);
        // 如果 enabled=false，可以把 next_run_time 置空；enabled=true 则重新计算 next_run_time
    }

    @Override
    public Task getById(Long taskId) {
        return taskMapper.selectById(taskId);
    }

    @Override
    public List<TaskTarget> listTargets(Long taskId) {
        return targetMapper.selectByTaskId(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logExecution(Long taskId,
                             Long deviceId,
                             LocalDateTime plannedTime,
                             LocalDateTime executeTime,
                             long durationMs,
                             String result,
                             String message) {
        // 从 Task 里拿 projectId
        Task task = getById(taskId);

        TaskExecutionLog log = new TaskExecutionLog();
        log.setTaskId(taskId);
        log.setProjectId(task.getProjectId());
        log.setDeviceId(deviceId);
        log.setPlannedTime(plannedTime);
        log.setExecuteTime(executeTime);
        log.setDurationMs(durationMs);
        log.setResult(result);
        log.setMessage(message);

        taskExecutionLogMapper.insertLog(log);
    }

    @Override
    public boolean deleteTaskById(long taskId) {
        long rows = taskMapper.deleteTaskById(taskId);
        return rows > 0;
    }

    @Override
    public boolean updateById(Task task) {
        return taskMapper.updateById(task) > 0;
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        return taskMapper.updateStatus(id, status) > 0;
    }

    @Override
    public void updateNextRunTime(Long taskId, LocalDateTime nextRunTime) {
        Task patch = new Task();
        patch.setId(taskId);
        patch.setNextRunTime(nextRunTime);
        patch.setStatus("已禁用");
        this.updateById(patch);
    }

    @Override
    public List<TaskDTO> selectTaskDTOById(Long taskId) {
        return taskMapper.selectTaskDTOsByTaskId(taskId);
    }

    @Override
    public void updateOperationConfig(Long id, String operationConfig) {
        Task task = new Task();
        task.setId(id);
        task.setOperationConfig(operationConfig);
        taskMapper.updateById(task);
    }

    @Override
    public void updateScheduleType(Long taskId, String ScheduleType) {
        Task task = new Task();
        task.setId(taskId);
        task.setOperationConfig(ScheduleType);
        taskMapper.updateById(task);
    }

    @Override
    public TaskSummaryDTO selectTaskSummary(Long taskId) {
        // 直接调用 Mapper，返回 DTO
        return taskExecutionLogMapper.selectTaskSummary(taskId);
    }

    @Override
    public List<DayCountDTO> selectTimeCounts(Long taskId, Integer days) {
        return taskExecutionLogMapper.selectTimeCounts(taskId, days);
    }

    @Override
    public IPage<LogDTO> getExecutionLogs(Long taskId, int pageNum, int pageSize) {
        Page<LogDTO> page = new Page<>(pageNum, pageSize);
        return taskExecutionLogMapper.selectLogs(page, taskId);
    }
}
