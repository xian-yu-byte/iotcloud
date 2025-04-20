package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceData;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttConfig;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MqttMapper {

    MqttConfig selectByProjectId();

//    List<MqttTopicConfig> selectByDeviceId(Integer deviceId);

    // 根据项目id查询mqtt主题配置
    MqttTopicConfig selectDeviceTopicByKey(@Param("userId") Long userId,
                                           @Param("projectId") Long projectId,
                                           @Param("deviceId") Long deviceId,
                                           @Param("topic") String topic);

    Long updateDeviceTopicByKey(MqttTopicConfigDTO dto);

    List<MqttTopicConfig> selectDeviceTopics(@Param("userId") Long userId,
                                             @Param("projectId") Long projectId,
                                             @Param("deviceId") Long deviceId);

    // 更新自定义的设备主题
//    int updateDeviceTopic(@Param("userId") Integer userId,
//                          @Param("projectId") Integer projectId,
//                          @Param("deviceId") Integer deviceId,
//                          @Param("topic") String topic);

    // 插入自定义的设备主题
    Long insertDeviceTopic(MqttTopicConfig config);

    //插入设备传递过来的数值
    void insertEmqxDeviceData(DeviceData deviceData);

    //删除主题
    Long deleteDeviceTopicById(@Param("id") Long id);
}
