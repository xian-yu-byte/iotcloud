package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.DTO.Device.EnumCount;
import com.atguigu.cloud.iotcloudspring.DTO.Device.ProjectDeviceStatsDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceStatusResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceStatusMapper {
    // 根据 deviceId 查询状态记录
    DeviceStatusResponse selectByDeviceId(@Param("id") Long id);

    // 根据 username 查询状态记录
//    Device selectByUsername(@Param("mqttusername") String mqttusername);

    // 插入设备状态记录
    int insertDeviceStatus(DeviceStatusResponse deviceStatus);

    // 更新设备状态记录
    int updateDeviceStatus(DeviceStatusResponse device);

    // 总数 + 在线 / 离线
    ProjectDeviceStatsDTO selectSummary(@Param("projectId") Long projectId);

    // accessCategory 分布
    List<EnumCount> groupByAccessCategory(@Param("projectId") Long projectId);

    // communicationMode 分布
    List<EnumCount> groupByCommMode(@Param("projectId") Long projectId);
}
