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
    boolean deleteDeviceType(Long deviceTypeId);

    // 返回所有设备类型
    List<DeviceTypeResponse> getDeviceTypeList(Long projectid);

    // 返回设备类型名称
    List<DeviceTypeNameResponse> getDeviceTypeNameList(Long projectid);

    // 创建设备类型属性
    Long createDeviceTypeAttribute(DeviceTypeAttributeDTO attributeDTO);

    // 根据设备类型id获取设备类型属性
    List<DeviceTypeAttributeResponse> getAttributesByDeviceTypeId(Long deviceTypeId);

    // 新增删除接口，返回 true 表示删除成功
    boolean deleteDeviceTypeAttribute(Long id);

    // 创建设备
    Long createDevice(DeviceDTO deviceDTO);

    // 根据设备id获取设备名称
    String getDeviceName(Long deviceId);

    //根据设备id删除设备
    boolean deleteDevice(Long deviceId);

    DeviceDetailResponse getDeviceDetail(Long deviceId);

    // 根据项目 ID 查询该项目下所有设备的详情
    List<DeviceDetailResponse> getDeviceDetailsByProject(Long projectId);

    // 根据设备类型id获取设备类型关联设备数据
    List<DeviceConnectResponse> getConnectedDevices(Long deviceTypeId);

    // 根据设备id获取设备关联设备类型数据
    String getDeviceTypeName(Long deviceTypeId);
}
