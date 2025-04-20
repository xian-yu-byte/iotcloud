package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceData;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceType;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceTypeAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    //创建设备类型及返回设备类型
    Long insertDeviceType(DeviceType deviceType);

    //删除设备类型
    Long deleteDeviceType(@Param("id") Long id);

    List<DeviceType> selectDeviceTypeList(@Param("projectid") Long projectid);

    // 返回设备类型名称
    List<DeviceType> selectDeviceTypeNameList(@Param("projectid") Long projectid);

    //创建设备类型属性,返回设备类型属性及删除
    Long insertDeviceTypeAttribute(DeviceTypeAttribute attribute);

    List<DeviceTypeAttribute> selectAttributesByDeviceTypeId(@Param("devicetypeid") Long devicetypeid);

    Long deleteDeviceTypeAttributeById(@Param("id") Long id);

    //创建设备,返回设备及删除
    Long insertDevice(Device device);

    String selectDeviceNameById(@Param("id") Long id);

    Long deleteDeviceById(@Param("id") Long id);

    Device selectDeviceById(@Param("id") Long id);

    DeviceTypeAttribute selectByTypeAndName(@Param("deviceid") Long deviceid,
                                            @Param("attributename") String attributename);


    // 根据设备类型ID查询设备类型信息
    DeviceType selectDeviceTypeById(@Param("id") Long id);

    // 根据设备ID查询设备上报的动态数据列表
    List<DeviceData> selectDeviceDataByDeviceId(@Param("deviceid") Long deviceid);

    // 根据项目ID查询设备列表
    List<Device> selectDevicesByProjectId(@Param("projectid") Long projectid);

    //根据设备类型id查询关联设备
    List<Device> selectDeviceByDeviceTypeId(@Param("devicetypeid") Long devicetypeid);

    //根据设备类型id查询设备类型名字
    String selectDeviceTypeNameById(@Param("id") Long id);

}
