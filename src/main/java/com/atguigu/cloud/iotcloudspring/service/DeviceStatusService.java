package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Device.ProjectDeviceStatsDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceStatusResponse;

public interface DeviceStatusService {

    DeviceStatusResponse updateOrInsertDeviceStatus(Long id, String devicestatus);

    ProjectDeviceStatsDTO getProjectStats(Long projectId);
}
