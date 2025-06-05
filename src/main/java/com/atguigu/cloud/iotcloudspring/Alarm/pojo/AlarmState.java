package com.atguigu.cloud.iotcloudspring.Alarm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("alarm_state")
public class AlarmState {

    @TableId(value = "alarm_id", type = IdType.INPUT)
    private Long alarmId;

    @TableField("device_id")
    private Long deviceId;

    @TableField("`count`")
    private Integer count = 0;

    @TableField("first_time")
    private LocalDateTime firstTime;

    @TableField("is_open")
    private Boolean isOpen = false;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public AlarmState(Long alarmId, Long deviceId) {
        this.alarmId   = alarmId;
        this.deviceId  = deviceId;
        this.count     = 0;
        this.firstTime = null;
        this.isOpen    = false;
    }
}
