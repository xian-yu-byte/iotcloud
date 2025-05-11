package com.atguigu.cloud.iotcloudspring.DTO.Device;

import com.atguigu.cloud.iotcloudspring.filter.enums.AccessCategory;
import com.atguigu.cloud.iotcloudspring.filter.enums.CommunicationMode;
import com.atguigu.cloud.iotcloudspring.filter.enums.ProtocolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTypeDTO {
    private Long id;
    private Long projectid;
    private String typename;
    private AccessCategory accesscategory;
    private CommunicationMode communicationmode;
    private ProtocolType protocol;
    private String dataformat;
}
