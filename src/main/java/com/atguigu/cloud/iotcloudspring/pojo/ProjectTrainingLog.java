package com.atguigu.cloud.iotcloudspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTrainingLog {
    private Long projectId;
    private LocalDateTime lastTrainedAt;
    private Integer LastRecordCount;
}
