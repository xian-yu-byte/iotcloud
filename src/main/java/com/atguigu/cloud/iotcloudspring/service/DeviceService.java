package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Device.*;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.*;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    //更新设备
    boolean updateDevice(DeviceDTO deviceDTO);

    // 根据设备id获取设备名称
    String getDeviceName(Long deviceId);

    // 根据设备类型名称获取设备类型ID
    Long getDeviceTypeId(String deviceTypeName);

    //根据设备id删除设备
    boolean deleteDevice(Long deviceId);

    DeviceDetailResponse getDeviceDetail(Long deviceId);

    // 根据项目 ID 查询该项目下所有设备的详情
    List<DeviceDetailResponse> getDeviceDetailsByProject(Long projectId);

    // 根据设备类型id获取设备类型关联设备数据
    List<DeviceConnectResponse> getConnectedDevices(Long deviceTypeId);

    // 根据设备id查询对应设备类型下的属性
    List<DeviceIDName> getAttributeNamesByDeviceId(Long deviceId);

    // 根据设备id获取设备关联设备类型数据
    String getDeviceTypeName(Long deviceTypeId);

    // 获取某设备、某属性的最新数据
    String getLatestData(String deviceKey, String fieldkey);

    Map<String, String> getLatestDatas(String deviceKey, List<String> fieldKeys);

    Device getByKey(String deviceKey);

    ProjectAdd getById(Long projectId);

    String createTopic(Long deviceId);

    List<DeviceAttributePointDTO> fetchHistoryByKey(String deviceKey,
                                                    String fieldKey,
                                                    Integer days,
                                                    LocalDateTime startTime,
                                                    LocalDateTime endTime);

    List<DeviceAttributePointDTO> fetchHistoryById(Long deviceId,
                                             Long attributeId,
                                             Integer days,
                                             LocalDateTime startTime,
                                             LocalDateTime endTime);

    //  获取设备类型详情
    DeviceTypeDTO getDeviceTypeDetailById(Long typeId);

    boolean updateDeviceType(DeviceTypeDTO deviceTypeDTO);

    //  获取属性详情
    DeviceTypeAttributeResponse getDeviceAttributeById(Long id);

    //  更新属性
    boolean updateDeviceAttribute(DeviceTypeAttributeDTO attributeDTO);

    // 根据设备id获取设备key
    String getDeviceKeyById(Long id);

    // 根据设备类型id和项目id获取设备id
    List<Long> listDeviceIdsByProjectAndType(Long projectId, Long deviceTypeId);
}
