package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeAttributeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeAttributeResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeNameResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeResponse;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
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
    public List<DeviceTypeResponse> getDeviceTypeList(Integer projectid) {
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
    public List<DeviceTypeNameResponse> getDeviceTypeNameList(Integer projectid) {
        List<DeviceType> list = deviceMapper.selectDeviceTypeNameList(projectid);
        return list.stream().map(device -> {
            DeviceTypeNameResponse response = new DeviceTypeNameResponse();
            response.setId(device.getId());
            response.setTypename(device.getTypename());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer createDeviceTypeAttribute(DeviceTypeAttributeDTO attributeDTO) {
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
    public List<DeviceTypeAttributeResponse> getAttributesByDeviceTypeId(Integer deviceTypeId) {
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
    public boolean deleteDeviceTypeAttribute(Integer id) {
        int rows = deviceMapper.deleteDeviceTypeAttributeById(id);
        return rows > 0;
    }

    @Override
    public Integer createDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDTO, device);
        deviceMapper.insertDevice(device);
        return device.getId();
    }

    @Override
    public DeviceResponse getDeviceById(Integer id) {
        Device device = deviceMapper.selectDeviceById(id);
        if (device == null) {
            return null;
        }
        DeviceResponse response = new DeviceResponse();
        BeanUtils.copyProperties(device, response);
        // 如果需要，可以通过 device.getDeviceTypeId() 关联查询设备类型属性并加入 response
        return response;
    }
}
