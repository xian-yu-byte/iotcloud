package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.DTO.Ai.AiDevice.DeviceReportReqDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AiDevice.DeviceReportRespDTO;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiDevice;

public interface AiDeviceService {
    /**
     * 根据MAC地址获取设备信息
     *
     * @param macAddress MAC地址
     * @return 设备信息
     */
    AiDevice getDeviceByMacAddress(String macAddress);

    /**
     * 根据Mac地址获取设备信息
     */
    AiDevice getDeviceById(String macAddress);

    /**
     * 根据设备ID获取激活码
     *
     * @param deviceId 设备ID
     * @return 激活码
     */
    String geCodeByDeviceId(String deviceId);

    /**
     * 检查设备是否激活
     */
    DeviceReportRespDTO checkDeviceActive(String macAddress, String clientId,
                                          DeviceReportReqDTO deviceReport);
}
