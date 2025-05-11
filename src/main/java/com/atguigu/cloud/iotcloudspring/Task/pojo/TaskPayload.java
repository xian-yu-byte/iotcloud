package com.atguigu.cloud.iotcloudspring.Task.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPayload {
    private Long id;
    private Long taskId;
    private Long projectId;
    private String paramKey;
    private String paramValue;
}
