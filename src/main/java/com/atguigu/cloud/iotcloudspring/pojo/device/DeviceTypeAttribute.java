package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTypeAttribute {
    private Integer id;
    private Integer devicetypeid;
    private String attributename;
    private String attributeunit;
    private String attributetype;
    private String datatype;
    private String attributedesc;
    private String expandoptions;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
}
