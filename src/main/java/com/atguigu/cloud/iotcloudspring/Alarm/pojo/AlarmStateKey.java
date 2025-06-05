package com.atguigu.cloud.iotcloudspring.Alarm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStateKey implements Serializable {
    private Long alarmId;
    private Long deviceId;
}
