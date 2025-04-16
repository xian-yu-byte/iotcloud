package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceStatusResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceStatusMapper {
    // 根据 deviceId 查询状态记录
    DeviceStatusResponse selectByDeviceId(@Param("id") Integer id);

    // 根据 username 查询状态记录
//    Device selectByUsername(@Param("mqttusername") String mqttusername);

    // 插入设备状态记录
    int insertDeviceStatus(DeviceStatusResponse deviceStatus);

    // 更新设备状态记录
    int updateDeviceStatus(DeviceStatusResponse device);
}
