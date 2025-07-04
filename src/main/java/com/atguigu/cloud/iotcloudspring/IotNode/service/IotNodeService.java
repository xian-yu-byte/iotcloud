package com.atguigu.cloud.iotcloudspring.IotNode.service;

import com.atguigu.cloud.iotcloudspring.IotNode.pojo.IotNode;

import java.util.List;

public interface IotNodeService {

    //创建一个新节点
    IotNode createNode(IotNode node);

    // 列出某个节点的所有直接子节点
    List<IotNode> listChildren(Long parentId);

    // 根据id删除节点
    void deleteNode(Long id);

    // 根据设备id删除节点
    void deleteDeviceNode(Long id);
}
