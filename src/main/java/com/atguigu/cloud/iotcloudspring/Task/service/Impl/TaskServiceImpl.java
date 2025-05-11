package com.atguigu.cloud.iotcloudspring.Task.service.Impl;

import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.CreateTaskDTO;
import com.atguigu.cloud.iotcloudspring.Task.DTO.CreateTaskRequest.TaskListItemDTO;
import com.atguigu.cloud.iotcloudspring.Task.mapper.TaskMapper;
import com.atguigu.cloud.iotcloudspring.Task.mapper.TaskPayloadMapper;
import com.atguigu.cloud.iotcloudspring.Task.mapper.TaskTargetMapper;
import com.atguigu.cloud.iotcloudspring.Task.pojo.Task;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskPayload;
import com.atguigu.cloud.iotcloudspring.Task.pojo.TaskTarget;
import com.atguigu.cloud.iotcloudspring.Task.service.TaskService;
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
}
