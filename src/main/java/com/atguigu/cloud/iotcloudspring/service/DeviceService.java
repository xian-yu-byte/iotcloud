package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeAttributeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeAttributeResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeNameResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeResponse;

import java.util.List;

public interface DeviceService {
    // 创建设备类型
    void createDeviceType(DeviceTypeDTO deviceTypeDto);

    // 返回所有设备类型
    List<DeviceTypeResponse> getDeviceTypeList(Integer projectid);

    // 返回设备类型名称
    List<DeviceTypeNameResponse> getDeviceTypeNameList(Integer projectid);

    // 创建设备类型属性
    Integer createDeviceTypeAttribute(DeviceTypeAttributeDTO attributeDTO);

    // 根据设备类型id获取设备类型属性
    List<DeviceTypeAttributeResponse> getAttributesByDeviceTypeId(Integer deviceTypeId);

    // 新增删除接口，返回 true 表示删除成功
    boolean deleteDeviceTypeAttribute(Integer id);

    // 创建设备
    Integer createDevice(DeviceDTO deviceDTO);

    //返回所有设备
    DeviceResponse getDeviceById(Integer id);
}
