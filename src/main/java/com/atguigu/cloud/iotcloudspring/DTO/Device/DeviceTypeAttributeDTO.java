package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeAttributeDTO {
    private Long id;
    private Long devicetypeid;
    private String attributename;
    private String displayname;   // 新增
    private String fieldkey;      // 新增
    private Boolean iscontrol;    // 新增
    private Boolean isquery;      // 新增
    private String attributeunit;
    private String attributetype;
    private String datatype;
    private String attributedesc;
    private String expandoptions;
}
