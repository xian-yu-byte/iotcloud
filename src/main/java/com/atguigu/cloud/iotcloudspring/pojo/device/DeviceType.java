package com.atguigu.cloud.iotcloudspring.pojo.device;

import com.atguigu.cloud.iotcloudspring.entity.enums.AccessCategory;
import com.atguigu.cloud.iotcloudspring.entity.enums.CommunicationMode;
import com.atguigu.cloud.iotcloudspring.entity.enums.ProtocolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceType {
    private Integer id;
    private Integer projectid;
    private String typename;
    private AccessCategory accesscategory;
    private CommunicationMode communicationmode;
    private ProtocolType protocol;
    private String dataformat;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;

}
