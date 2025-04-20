package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeAttributeDTO {
    private Long devicetypeid;
    private String attributename;
    private String attributeunit;
    private String attributetype;
    private String datatype;       // 建议使用 'STRING', 'INT', 'FLOAT' 等字符串
    private String attributedesc;
    private String expandoptions;
}
