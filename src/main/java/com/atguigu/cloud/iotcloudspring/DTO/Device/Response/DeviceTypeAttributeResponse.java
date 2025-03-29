package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTypeAttributeResponse {
    private Integer id;
    private Integer devicetypeid;
    private String attributename;
    private String attributeunit;
    private String attributetype;
    private String datatype;
    private String attributedesc;
    private String expandoptions;
}
