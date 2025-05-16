package com.atguigu.cloud.iotcloudspring.Rule.pojo;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleLog {
    private Long Id;
    private Long ruleId;
    private Long deviceId;
    private Long dateId;
    private BigDecimal value;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime triggerTime;
    private String actionStatus;
    private String message;
}
