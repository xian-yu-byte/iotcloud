package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.Common.dao.BaseDao;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

@Mapper
@Primary
public interface AiMapper extends BaseDao<AiAgent> {
    /**
     * 获取智能体的设备数量
     *
     * @param agentId 智能体ID
     * @return 设备数量
     */

    Integer getDeviceCountByAgentId(@Param("agentId") String agentId);

    Integer selectMaxSort();
}
