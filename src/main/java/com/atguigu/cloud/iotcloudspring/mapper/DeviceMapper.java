package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceType;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceTypeAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    //创建设备类型及返回设备类型
    int insertDeviceType(DeviceType deviceType);
    List<DeviceType> selectDeviceTypeList(@Param("projectid") Integer projectid);

    // 返回设备类型名称
    List<DeviceType> selectDeviceTypeNameList(@Param("projectid") Integer projectid);

    //创建设备类型属性,返回设备类型属性及删除
    int insertDeviceTypeAttribute(DeviceTypeAttribute attribute);
    List<DeviceTypeAttribute> selectAttributesByDeviceTypeId(@Param("devicetypeid") Integer devicetypeid);
    int deleteDeviceTypeAttributeById(@Param("id") Integer id);

    //创建设备,返回设备及删除
    int insertDevice(Device device);
    Device selectDeviceById(@Param("id") Integer id);
}
