package com.atguigu.cloud.iotcloudspring.Task.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTarget {
    private Long id;
    private Long taskId;
    private Long projectId;
    private String targetType;
    private Long targetId;
}
