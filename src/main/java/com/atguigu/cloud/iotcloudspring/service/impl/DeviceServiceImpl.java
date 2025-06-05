package com.atguigu.cloud.iotcloudspring.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.atguigu.cloud.iotcloudspring.DTO.Device.*;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.*;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.mapper.ICMapper;
import com.atguigu.cloud.iotcloudspring.mapper.MqttMapper;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.device.*;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {
    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private MqttMapper mqttMapper;

    @Resource
    private ICMapper icMapper;

    @Resource
    private Snowflake snowflake;

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
        attribute.setDisplayname(attributeDTO.getDisplayname());   // 新增
        attribute.setFieldkey(attributeDTO.getFieldkey());         // 新增
        attribute.setIscontrol(attributeDTO.getIscontrol() != null && attributeDTO.getIscontrol() ? 1 : 0);
        attribute.setIsquery(attributeDTO.getIsquery() != null && attributeDTO.getIsquery() ? 1 : 0);
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
            response.setDisplayname(attr.getDisplayname());  // 新增
            response.setFieldkey(attr.getFieldkey());        // 新增
            response.setIscontrol(attr.getIscontrol());      // 新增
            response.setIsquery(attr.getIsquery());          // 新增
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
        String deviceKey = "device_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        BeanUtils.copyProperties(deviceDTO, device);
        device.setMqttusername("ceshi");
        device.setMqttpassword("123456");
        device.setDevicekey(deviceKey);
        deviceMapper.insertDevice(device);
        return device.getId();
    }

    @Override
    public boolean updateDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDTO, device);
        Long rows = deviceMapper.updateDevice(device);
        return rows > 0;
    }

    @Override
    public String getDeviceName(Long id) {
        return deviceMapper.selectDeviceNameById(id);
    }

    @Override
    public Long getDeviceTypeId(String deviceTypeName) {
        return deviceMapper.selectDeviceNameByName(deviceTypeName);
    }

    @Override
    public boolean deleteDevice(Long id) {
        Long rows = deviceMapper.deleteDeviceById(id);
        return rows > 0;
    }

    // 后续需要优化
    @Override
    public DeviceDetailResponse getDeviceDetail(Long deviceId) {
        // 查询设备静态信息
        Device device = deviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            return null; // 可根据需要抛出异常或返回特定错误信息
        }

        DeviceDetailResponse response = new DeviceDetailResponse();
        response.setId(device.getId());
        response.setDevicename(device.getDevicename());
        response.setDevicekey(device.getDevicekey());
        response.setProjectid(device.getProjectid());
        response.setDevicelocation(device.getDevicelocation());
        response.setDevicecommunication(device.getDevicecommunication());
        response.setDeviceinformation(device.getDeviceinformation());
        response.setDevicestatus(device.getDevicestatus());
        response.setDevicetypeid(device.getDevicetypeid());

        // 根据 deviceTypeId 查询设备类型名称
        DeviceType deviceType = deviceMapper.selectDeviceTypeById(device.getDevicetypeid());
        if (deviceType != null) {
            response.setDevicetypename(deviceType.getTypename());
        }

        // 查询设备动态数据
        List<DeviceData> dataList = deviceMapper.selectDeviceDataByDeviceId(deviceId);
        Map<Long, DeviceData> latestByAttr = dataList.stream()
                .collect(Collectors.toMap(
                        DeviceData::getDevicetypeattributeid,
                        Function.identity(),
                        (d1, d2) ->
                                d1.getTimestamp().isAfter(d2.getTimestamp()) ? d1 : d2
                ));

        // 把每个属性最新的数据映射成 Response 对象
        List<DeviceDataResponse> deviceDataResponses = latestByAttr.values().stream()
                .map(data -> {
                    DeviceDataResponse d = new DeviceDataResponse();
                    BeanUtils.copyProperties(data, d);
                    return d;
                })
                .collect(Collectors.toList());

        response.setDevicedata(deviceDataResponses);


        // 查询该设备类型定义的属性
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
    public List<DeviceIDName> getAttributeNamesByDeviceId(Long deviceId) {
        return deviceMapper.selectAttributeNamesByDeviceId(deviceId);
    }

    @Override
    public String getDeviceTypeName(Long id) {
        return deviceMapper.selectDeviceTypeNameById(id);
    }

    @Override
    public String getLatestData(String deviceKey, String fieldkey) {
        return deviceMapper.selectLatestValueByDeviceKeyAndFieldKey(deviceKey, fieldkey);
    }

    @Override
    public Map<String, String> getLatestDatas(
            String deviceKey, List<String> fieldKeys
    ) {
        List<DeviceDataFieldKeysDTO> list = deviceMapper
                .selectDeviceByDeviceKeyAndFieldKeys(deviceKey, fieldKeys);

        return list.stream()
                .collect(Collectors.toMap(
                        DeviceDataFieldKeysDTO::getFieldKey,
                        DeviceDataFieldKeysDTO::getValue
                ));
    }

    @Override
    public Long setTemplate(FieldTemplate Template) {
        deviceMapper.insertFieldTemplate(Template);
        return Template.getId();
    }

    @Override
    public Device getByKey(String deviceKey) {
        return deviceMapper.selectByDeviceKey(deviceKey);
    }

    @Override
    public ProjectAdd getById(Long projectId) {
        return icMapper.selectProjectById(projectId);
    }

    @Override
    public String createTopic(Long deviceId) {
        // 1. 先查 device 表
        Device device = deviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            throw new IllegalArgumentException("Device not found: " + deviceId);
        }

        Long projectId = device.getProjectid();
        String deviceKey = device.getDevicekey();
        if (deviceKey == null || deviceKey.isEmpty()) {
            throw new IllegalArgumentException("DeviceKey is empty for deviceId: " + deviceId);
        }

        // 2. 用 deviceKey 查询 mqtt_topic_config
        int cnt = mqttMapper.countByDeviceKey(deviceKey);
        if (cnt > 0) {
            throw new IllegalArgumentException("Topic already exists for deviceKey: ");
        }

        // 3. 查 project 表，拿 userId
        ProjectAdd project = icMapper.selectProjectById(projectId);
        if (project == null || project.getUserid() == null) {
            throw new IllegalArgumentException("Project or user not found for projectId: " + projectId);
        }
        Long userId = project.getUserid();

        // 4. 组装 topic
        String fullTopic = String.format(
                "user%s/project%s/device%s/%s",
                userId, projectId, deviceId, deviceKey
        );

        // 5. 插入 mqtt_topic_config 表

        try {
            mqttMapper.insertNew(
                    userId, projectId, deviceId, deviceKey,
                    fullTopic, "下发", "", true
            );
        } catch (Exception e) {
            log.error("e: ", e);
            throw new RuntimeException("插入订阅主题失败，请检查参数或数据库状态！");
        }

        // 6. 返回 topic
        return fullTopic;
    }

    @Override
    public List<DeviceAttributePointDTO> fetchHistoryByKey(String deviceKey,
                                                           String fieldKey,
                                                           Integer days,
                                                           LocalDateTime startTime,
                                                           LocalDateTime endTime) {
        // 计算时间区间（同之前例子）
        LocalDateTime[] range = calcRange(days, startTime, endTime);
        return deviceMapper.selectHistoryByKey(deviceKey, fieldKey, range[0], range[1]);
    }

    @Override
    public List<DeviceAttributePointDTO> fetchHistoryById(Long deviceId,
                                                          Long attributeId,
                                                          Integer days,
                                                          LocalDateTime startTime,
                                                          LocalDateTime endTime) {
        LocalDateTime[] range = calcRange(days, startTime, endTime);
        return deviceMapper.selectHistoryById(deviceId, attributeId, range[0], range[1]);
    }

    //  获取设备类型详情
    @Override
    public DeviceTypeDTO getDeviceTypeDetailById(Long typeId) {
        return deviceMapper.selectTypeDetailById(typeId);
    }

    //  更新设备类型
    @Override
    public boolean updateDeviceType(DeviceTypeDTO deviceTypeDTO) {
        return deviceMapper.updateDeviceType(deviceTypeDTO) > 0;
    }

    //  获取属性详情
    @Override
    public DeviceTypeAttributeResponse getDeviceAttributeById(Long id) {
        return deviceMapper.selectDeviceAttributeById(id);
    }

    //  更新属性
    @Override
    public boolean updateDeviceAttribute(DeviceTypeAttributeDTO attributeDTO) {
        return deviceMapper.updateDeviceAttribute(attributeDTO) > 0;
    }

    /**
     * 计算 startTime/endTime，如果未传就用 days（默认为1）
     **/
    private LocalDateTime[] calcRange(Integer days, LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return new LocalDateTime[]{start, end};
        }
        int d = (days != null && days > 0) ? days : 1;
        LocalDateTime now = LocalDateTime.now();
        return new LocalDateTime[]{now.minusDays(d), now};
    }

    @Override
    public String getDeviceKeyById(Long id) {
        return deviceMapper.deviceIdSelectDeviceKeyById(id);
    }

    @Override
    public List<Long> listDeviceIdsByProjectAndType(Long projectId, Long deviceTypeId) {
        return deviceMapper.listDeviceIdsByProjectAndType(projectId, deviceTypeId);
    }

    @Override
    public List<MessageCountDTO> getMessageCounts(Long deviceId,
                                                  LocalDateTime start,
                                                  LocalDateTime end,
                                                  String interval) {
        return deviceMapper.selectMessageCounts(deviceId, start, end, interval);
    }

    @Override
    public List<MessageLatencyDTO> getMessageLatency(Long deviceId,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     String interval) {
        return deviceMapper.selectMessageLatency(deviceId, start, end, interval);
    }

    @Override
    public List<DeviceDataDTO> getDataByProjectId(Long projectId) {
        List<Long> deviceIds = deviceMapper.selectDeviceIdsByProjectId(projectId);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        return deviceMapper.selectDataByDeviceIds(deviceIds);
    }
}
