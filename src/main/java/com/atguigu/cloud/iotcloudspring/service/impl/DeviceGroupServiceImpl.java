package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.mapper.DeviceGroupMapper;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceGroup;
import com.atguigu.cloud.iotcloudspring.service.DeviceGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {

    @Resource
    private DeviceGroupMapper deviceGroupMapper;

    @Override
    public List<DeviceGroup> getDeviceGroupList(Long projectId) {
        return deviceGroupMapper.getDeviceGroupList(projectId);
    }

    @Override
    public void createDeviceGroup(DeviceGroup deviceGroup) {
        deviceGroupMapper.createDeviceGroup(deviceGroup);
    }

    @Override
    public List<Device> getDeviceList(Long groupId) {
        return deviceGroupMapper.getDeviceList(groupId);
    }

    @Override
    public Boolean updateDeviceGroup(DeviceGroup newDeviceGroup) {
        return deviceGroupMapper.updateDeviceGroup(newDeviceGroup)>0;
    }

    @Override
    public Boolean deleteDeviceGroup(Long id) {
//        List<Device> devices = deviceGroupMapper.getDeviceList(id);
//        if (!devices.isEmpty()) return false;
        return deviceGroupMapper.deleteDeviceGroup(id)>0;
    }

    @Override
    public void addDeviceToGroup(Long groupId, Long deviceId) {
        deviceGroupMapper.addDeviceToGroup(groupId, deviceId);
    }

    @Override
    public Boolean removeDeviceFromGroup(Long groupId, Long deviceId) {
        return deviceGroupMapper.removeDeviceFromGroup(groupId, deviceId)>0;
    }


}
