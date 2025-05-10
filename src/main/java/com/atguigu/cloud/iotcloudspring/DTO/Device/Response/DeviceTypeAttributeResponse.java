package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTypeAttributeResponse {
    private Long id;
    private Long devicetypeid;
    private String attributename;
    private String displayname;  // 新增
    private String fieldkey;     // 新增
    private Integer iscontrol;   // 新增
    private Integer isquery;     // 新增
    private String attributeunit;
    private String attributetype;
    private String datatype;
    private String attributedesc;
    private String expandoptions;
}
