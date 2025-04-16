package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceStatusResponse;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;

public interface DeviceStatusService {
    DeviceStatusResponse updateOrInsertDeviceStatus(Integer id, String devicestatus);
}
