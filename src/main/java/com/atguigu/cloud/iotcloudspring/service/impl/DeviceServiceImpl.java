package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeAttributeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.*;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceData;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceType;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceTypeAttribute;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public void createDeviceType(DeviceTypeDTO deviceTypeDto) {
        // 将 DTO 转换为实体对象
        DeviceType deviceType = new DeviceType();
        deviceType.setProjectid(deviceTypeDto.getProjectid());
        deviceType.setTypename(deviceTypeDto.getTypename());
        deviceType.setAccesscategory(deviceTypeDto.getAccesscategory());
        deviceType.setCommunicationmode(deviceTypeDto.getCommunicationmode());
        deviceType.setProtocol(deviceTypeDto.getProtocol());
        deviceType.setDataformat(deviceTypeDto.getDataformat());
        // 调用 Mapper 执行插入
        deviceMapper.insertDeviceType(deviceType);
    }

    @Override
    public boolean deleteDeviceType(Long id) {
        Long rows = deviceMapper.deleteDeviceType(id);
        return rows > 0;
    }

    @Override
    public List<DeviceTypeResponse> getDeviceTypeList(Long projectid) {
        List<DeviceType> list = deviceMapper.selectDeviceTypeList(projectid);
        // 将 DeviceType 转换为返回 DTO DeviceTypeResponse
        return list.stream().map(device -> {
            DeviceTypeResponse response = new DeviceTypeResponse();
            response.setId(device.getId());
            response.setTypename(device.getTypename());
            response.setAccesscategory(device.getAccesscategory());
            response.setProtocol(device.getProtocol());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DeviceTypeNameResponse> getDeviceTypeNameList(Long projectid) {
        List<DeviceType> list = deviceMapper.selectDeviceTypeNameList(projectid);
        return list.stream().map(device -> {
            DeviceTypeNameResponse response = new DeviceTypeNameResponse();
            response.setId(device.getId());
            response.setTypename(device.getTypename());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public Long createDeviceTypeAttribute(DeviceTypeAttributeDTO attributeDTO) {
        DeviceTypeAttribute attribute = new DeviceTypeAttribute();
        attribute.setDevicetypeid(attributeDTO.getDevicetypeid());
        attribute.setAttributename(attributeDTO.getAttributename());
        attribute.setAttributeunit(attributeDTO.getAttributeunit());
        attribute.setDatatype(attributeDTO.getDatatype());
        attribute.setAttributetype(attributeDTO.getAttributetype());
        attribute.setAttributedesc(attributeDTO.getAttributedesc());
        attribute.setExpandoptions(attributeDTO.getExpandoptions());

        deviceMapper.insertDeviceTypeAttribute(attribute);
        return attribute.getId();
    }

    @Override
    public List<DeviceTypeAttributeResponse> getAttributesByDeviceTypeId(Long deviceTypeId) {
        List<DeviceTypeAttribute> list = deviceMapper.selectAttributesByDeviceTypeId(deviceTypeId);
        return list.stream().map(attr -> {
            DeviceTypeAttributeResponse response = new DeviceTypeAttributeResponse();
            response.setId(attr.getId());
            response.setDevicetypeid(attr.getDevicetypeid());
            response.setAttributename(attr.getAttributename());
            response.setAttributeunit(attr.getAttributeunit());
            response.setDatatype(attr.getDatatype());
            response.setAttributetype(attr.getAttributetype());
            response.setAttributedesc(attr.getAttributedesc());
            response.setExpandoptions(attr.getExpandoptions());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean deleteDeviceTypeAttribute(Long id) {
        Long rows = deviceMapper.deleteDeviceTypeAttributeById(id);
        return rows > 0;
    }

    @Override
    public Long createDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDTO, device);
        deviceMapper.insertDevice(device);
        return device.getId();
    }

    @Override
    public String getDeviceName(Long id) {
        return deviceMapper.selectDeviceNameById(id);
    }

    @Override
    public boolean deleteDevice(Long id) {
        Long rows = deviceMapper.deleteDeviceById(id);
        return rows > 0;
    }

    @Override
    public DeviceDetailResponse getDeviceDetail(Long deviceId) {
        // 1. 查询设备静态信息
        Device device = deviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            return null; // 可根据需要抛出异常或返回特定错误信息
        }

        DeviceDetailResponse response = new DeviceDetailResponse();
        response.setId(device.getId());
        response.setDevicename(device.getDevicename());
        response.setDevicekey(device.getDevicekey());
        response.setProjectid(device.getProjectid());

        // 2. 根据 deviceTypeId 查询设备类型名称
        DeviceType deviceType = deviceMapper.selectDeviceTypeById(device.getDevicetypeid());
        if (deviceType != null) {
            response.setDevicetypename(deviceType.getTypename());
        }

        // 3. 查询设备动态数据（例如，根据设备ID查询最新上报数据）
        List<DeviceData> dataList = deviceMapper.selectDeviceDataByDeviceId(deviceId);
        List<DeviceDataResponse> deviceDataResponses = dataList.stream().map(data -> {
            DeviceDataResponse d = new DeviceDataResponse();
            BeanUtils.copyProperties(data, d);
            return d;
        }).collect(Collectors.toList());
        response.setDevicedata(deviceDataResponses);

        // 4. 查询该设备类型定义的属性
        List<DeviceTypeAttribute> attributes = deviceMapper.selectAttributesByDeviceTypeId(device.getDevicetypeid());
        List<DeviceTypeAttributeResponse> attributeResponses = attributes.stream().map(attr -> {
            DeviceTypeAttributeResponse a = new DeviceTypeAttributeResponse();
            BeanUtils.copyProperties(attr, a);
            return a;
        }).collect(Collectors.toList());
        response.setAttributes(attributeResponses);

        return response;
    }

    @Override
    public List<DeviceDetailResponse> getDeviceDetailsByProject(Long projectId) {
        // 首先，根据项目ID查询所有设备
        List<Device> devices = deviceMapper.selectDevicesByProjectId(projectId);
        // 对于每个设备，调用 getDeviceDetail(device.getId())
        return devices.stream()
                .map(device -> getDeviceDetail(device.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceConnectResponse> getConnectedDevices(Long deviceTypeId) {
        List<Device> devices = deviceMapper.selectDeviceByDeviceTypeId(deviceTypeId);
        return devices.stream().map(device -> {
            DeviceConnectResponse dto = new DeviceConnectResponse();
            dto.setId(device.getId());
            dto.setDevicename(device.getDevicename());
            dto.setDevicestatus(device.getDevicestatus());
            dto.setUpdatetime(device.getUpdatetime());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public String getDeviceTypeName(Long id) {
        return deviceMapper.selectDeviceTypeNameById(id);
    }
}
