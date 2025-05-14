package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceGroupMapper {
    List<DeviceGroup> getDeviceGroupList(Long projectId);

    void createDeviceGroup(DeviceGroup deviceGroup);

    List<Device> getDeviceList(Long groupId);

    int updateDeviceGroup(DeviceGroup newDeviceGroup);

    int deleteDeviceGroup(Long id);

    void addDeviceToGroup(Long groupId, Long deviceId);

    int removeDeviceFromGroup(Long groupId, Long deviceId);
}
