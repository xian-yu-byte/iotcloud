package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeAttributeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.*;

import java.util.List;

public interface DeviceService {
    // 创建设备类型
    void createDeviceType(DeviceTypeDTO deviceTypeDto);

    // 删除设备类型
    boolean deleteDeviceType(Integer deviceTypeId);

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

    // 根据设备id获取设备名称
    String getDeviceName(Integer deviceId);

    //根据设备id删除设备
    boolean deleteDevice(Integer deviceId);

    DeviceDetailResponse getDeviceDetail(Integer deviceId);

    // 根据项目 ID 查询该项目下所有设备的详情
    List<DeviceDetailResponse> getDeviceDetailsByProject(Integer projectId);

    // 根据设备类型id获取设备类型关联设备数据
    List<DeviceConnectResponse> getConnectedDevices(Integer deviceTypeId);

    // 根据设备id获取设备关联设备类型数据
    String getDeviceTypeName(Integer deviceTypeId);
}
