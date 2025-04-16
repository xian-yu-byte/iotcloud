package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceStatusResponse;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceStatusMapper;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.service.DeviceStatusService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {
    @Resource
    private DeviceStatusMapper deviceStatusMapper;

    @Override
    @Transactional
    public DeviceStatusResponse updateOrInsertDeviceStatus(Integer id, String devicestatus) {
        // 先根据设备 id 查询记录
        DeviceStatusResponse deviceStatusResponse = deviceStatusMapper.selectByDeviceId(id);
        if (deviceStatusResponse == null) {
            // 如果不存在，则创建新的设备记录（注意：实际业务中，设备记录一般应该提前创建）
            deviceStatusResponse = new DeviceStatusResponse();
            deviceStatusResponse.setId(id);
            deviceStatusResponse.setDevicestatus(devicestatus);
            deviceStatusMapper.insertDeviceStatus(deviceStatusResponse);
            System.out.println("插入新的设备状态：id=" + id + ", status=" + devicestatus);
        } else {
            // 如果存在，则根据当前状态判断是否需要更新（本例中不做判断，直接更新）
            deviceStatusResponse.setDevicestatus(devicestatus);
            deviceStatusMapper.updateDeviceStatus(deviceStatusResponse);
            System.out.println("更新设备状态：id=" + id + ", status=" + devicestatus);
        }
        return deviceStatusResponse;
    }
}
