package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import com.atguigu.cloud.iotcloudspring.entity.enums.AccessCategory;
import com.atguigu.cloud.iotcloudspring.entity.enums.ProtocolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeResponse {
    private Integer id;
    private String typename;
    private AccessCategory accesscategory;
    private ProtocolType protocol;
}
