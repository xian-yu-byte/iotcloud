package com.atguigu.cloud.iotcloudspring.DTO;

import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeAttributeResponse;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceTypeAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDTO {
    private Long userid;
    private Long projectid;
    private Long id;
    private Long devicetypeid;
    private String devicename;
    private String typename;
    private String devicelocation;
    private List<DeviceTypeAttribute> attributes;
}
