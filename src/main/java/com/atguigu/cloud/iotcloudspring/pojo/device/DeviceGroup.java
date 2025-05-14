package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceGroup {
    private Long id;
    private Long projectid;
    private String groupname;
    private String description;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
}
