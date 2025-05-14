package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceGroup;

import java.util.List;

public interface DeviceGroupService{
    List<DeviceGroup> getDeviceGroupList(Long projectId);

    void createDeviceGroup(DeviceGroup deviceGroup);

    List<Device> getDeviceList(Long groupId);

    Boolean updateDeviceGroup(DeviceGroup newDeviceGroup);

    Boolean deleteDeviceGroup(Long id);

    void addDeviceToGroup(Long groupId, Long deviceId);

    Boolean removeDeviceFromGroup(Long groupId, Long deviceId);
}
