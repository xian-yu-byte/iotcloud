package com.atguigu.cloud.iotcloudspring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingLog {
    private Long projectId;
    private LocalDateTime lastTrainedAt;
    private Integer lastRecordCount;
}
