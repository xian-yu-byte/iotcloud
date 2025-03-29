package com.atguigu.cloud.iotcloudspring.DTO.Device;

import com.atguigu.cloud.iotcloudspring.entity.enums.AccessCategory;
import com.atguigu.cloud.iotcloudspring.entity.enums.CommunicationMode;
import com.atguigu.cloud.iotcloudspring.entity.enums.ProtocolType;
import lombok.Data;

@Data
public class DeviceTypeDTO {
    private Integer projectid;
    private String typename;
    private AccessCategory accesscategory;
    private CommunicationMode communicationmode;
    private ProtocolType protocol;
    private String dataformat;
}
