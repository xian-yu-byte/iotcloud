package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.DTO.Device.*;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeAttributeResponse;
import com.atguigu.cloud.iotcloudspring.DTO.IdDTO;
import com.atguigu.cloud.iotcloudspring.pojo.device.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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

    //创建设备,返回设备及删除，修改
    Long insertDevice(Device device);

    String selectDeviceNameById(@Param("id") Long id);

    Long selectDeviceNameByName(@Param("name") String name);

    Long deleteDeviceById(@Param("id") Long id);

    Device selectDeviceById(@Param("id") Long id);

    Long updateDevice(Device device);

    /**
     * 根据 deviceKey 查设备
     */
    Device selectByDeviceKey(@Param("deviceKey") String deviceKey);

    DeviceTypeAttribute selectByTypeAndName(@Param("deviceTypeId") Long deviceTypeId,
                                            @Param("attributename") String attributename);


    // 根据设备类型ID查询设备类型信息
    DeviceType selectDeviceTypeById(@Param("id") Long id);

    // 根据设备ID查询设备上报的动态数据列表
    List<DeviceData> selectDeviceDataByDeviceId(@Param("deviceid") Long deviceid);

    // 根据项目ID查询设备列表
    List<Device> selectDevicesByProjectId(@Param("projectid") Long projectid);

    // 根据设备类型id查询关联设备
    List<Device> selectDeviceByDeviceTypeId(@Param("devicetypeid") Long devicetypeid);

    // 根据设备id查询对应设备类型下的属性
    List<DeviceIDName> selectAttributeNamesByDeviceId(@Param("deviceId") Long deviceId);

    //根据设备类型id查询设备类型名字
    String selectDeviceTypeNameById(@Param("id") Long id);

    // 查询设备三元设备id
    IdDTO selectDeviceKeyById(@Param("devicekey") String devicekey);

    // 根绝设备id查询devicekey
    String selectDeviceKeyByDeviceId(@Param("deviceid") Long deviceid);

    // 根据 deviceKey 和 attributeName 查最新一条 devicedata 记录
    String selectLatestValueByDeviceKeyAndFieldKey(
            @Param("deviceKey") String deviceKey,
            @Param("fieldkey") String fieldkey
    );

    // 根据 deviceKey 和 attributeName 查最新多条 devicedata 记录
    List<DeviceDataFieldKeysDTO> selectDeviceByDeviceKeyAndFieldKeys(
            @Param("deviceKey") String deviceKey,
            @Param("fieldKeys") List<String> fieldKeys
    );

    // 根据项目id个性化设置不同的用户的字段模板
    int insertFieldTemplate(FieldTemplate Template);

    /**
     * 查询某设备、某属性在指定时间区间内的历史数据
     *
     * @param deviceKey 设备唯一标识
     * @param fieldKey  属性英文 key（或 datakey）
     * @param startTime 起始时间（包含）
     * @param endTime   结束时间（包含）
     */
    List<DeviceAttributePointDTO> selectHistoryByKey(
            @Param("deviceKey") String deviceKey,
            @Param("fieldKey") String fieldKey,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // 上面的第二张方法
    List<DeviceAttributePointDTO> selectHistoryById(
            @Param("deviceId") Long deviceId,
            @Param("attributeId") Long attributeId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // 根据设备类型id查询设备类型详情
    DeviceTypeDTO selectTypeDetailById(Long typeId);

    //  更新设备类型
    int updateDeviceType(DeviceTypeDTO deviceTypeDTO);

    // 根据设备类型属性id查询设备类型属性详情
    DeviceTypeAttributeResponse selectDeviceAttributeById(Long id);

    // 更新属性
    int updateDeviceAttribute(DeviceTypeAttributeDTO attributeDTO);

    // 根据设备id查询deviceKey
    String deviceIdSelectDeviceKeyById(@Param("id") Long id);

    //只查询某项目下、某设备类型的所有设备 ID
    List<Long> listDeviceIdsByProjectAndType(Long projectId, Long deviceTypeId);

    //上下行消息统计
    List<MessageCountDTO> selectMessageCounts(@Param("deviceId") Long deviceId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime,
                                              @Param("interval") String interval);

    // 延时统计（只看上行 UP）
    List<MessageLatencyDTO> selectMessageLatency(@Param("deviceId") Long deviceId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime,
                                                 @Param("interval") String interval);

    // 根据 projectId 查询所有设备 ID
    List<Long> selectDeviceIdsByProjectId(@Param("projectId") Long projectId);

    // 根据设备 ID 列表，按时间升序查 dataKey、dataValue、timestamp
    List<DeviceDataDTO> selectDataByDeviceIds(@Param("deviceIds") List<Long> deviceIds);

    // 统计自某时间以来新增记录数
    int countSince(
            @Param("projectId") Long projectId,
            @Param("since") LocalDateTime since
    );

    // 拉取用于训练的全部历史数据（可加时间窗）
    List<DataPointDTO> selectForTraining(@Param("projectId") Long projectId);
}
