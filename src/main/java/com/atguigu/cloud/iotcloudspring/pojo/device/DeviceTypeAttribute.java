package com.atguigu.cloud.iotcloudspring.pojo.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTypeAttribute {
    private Long id;
    private Long devicetypeid;
    private String fieldkey;      // 新增
    private String attributename;
    private String attributeunit;
    private String attributetype;
    private String datatype;
    private String attributedesc;
    private String expandoptions;
    private String displayname;   // 新增
    private Integer iscontrol;    // 新增
    private Integer isquery;      // 新增
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
}
