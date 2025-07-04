package com.atguigu.cloud.iotcloudspring.IotNode.mapper;

import com.atguigu.cloud.iotcloudspring.IotNode.pojo.IotNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IotNodeMapper {

    //插入新节点，create_time/update_time 由数据库 DEFAULT/CURRENT_TIMESTAMP 管理
    int insert(IotNode node);

    //根据主键查询
    IotNode selectById(@Param("id") Long id);

    //根据 parentId 查询所有直接子节点
    List<IotNode> findByParentId(@Param("parentId") Long parentId);

    // 根据主键删除节点
    int deleteById(@Param("id") Long id);

    // 根绝设备id删除节点
    int updateDeviceNode(@Param("id") Long id);
}