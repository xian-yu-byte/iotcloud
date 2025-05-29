package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.DTO.Mqtt.MqttTopicConfigDTO;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceData;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceMessageLog;
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

    /**
     * 判断是否已存在该 deviceId 的配置
     */
    int countByDeviceKey(@Param("deviceKey") String deviceKey);

    /**
     * 插入一条新的 mqtt_topic_config 记录
     */
    int insertNew(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId,
            @Param("deviceId") Long deviceId,
            @Param("deviceKey") String deviceKey,
            @Param("topic") String topic,
            @Param("topicType") String topicType,
            @Param("description") String description,
            @Param("effective") Boolean effective
    );

    //插入设备传递过来的数值
    void insertEmqxDeviceData(DeviceData deviceData);

    // 插入一条日志
    void insertMessageLog(DeviceMessageLog log);

    // 如果不存在就插入，否则累加 up_count/down_count
    void upsertMessageStat(@Param("deviceId") Long deviceId,
                           @Param("up") int up,
                           @Param("down") int down);

    //删除主题
    Long deleteDeviceTopicById(@Param("id") Long id);
}
