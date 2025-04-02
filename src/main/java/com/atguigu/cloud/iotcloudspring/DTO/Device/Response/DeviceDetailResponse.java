package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDetailResponse {
    private Integer id;
    private String devicename;
    private String devicekey;
    private Integer projectid;

    // 设备类型信息
    private String devicetypename;

    // 设备动态数据（例如上报的各个属性的最新数据，按需求你可以设计为 List 或 Map）
    private List<DeviceDataResponse> devicedata;

    // 该设备类型定义的属性（例如温度、湿度等）
    private List<DeviceTypeAttributeResponse> attributes;
}
