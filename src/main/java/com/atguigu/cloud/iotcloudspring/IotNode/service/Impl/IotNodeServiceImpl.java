package com.atguigu.cloud.iotcloudspring.IotNode.service.Impl;

import com.atguigu.cloud.iotcloudspring.IotNode.mapper.IotNodeMapper;
import com.atguigu.cloud.iotcloudspring.IotNode.pojo.IotNode;
import com.atguigu.cloud.iotcloudspring.IotNode.service.IotNodeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class IotNodeServiceImpl implements IotNodeService {

    private final IotNodeMapper iotNodeMapper;

    @Override
    @Transactional
    public IotNode createNode(IotNode node) {
        // 设置创建/更新时间（如果数据库已自动管理，可省略）
        LocalDateTime now = LocalDateTime.now();
        node.setCreateTime(now);
        node.setUpdateTime(now);

        iotNodeMapper.insert(node);
        // insert 后，node.id 会被 MyBatis 回填
        return iotNodeMapper.selectById(node.getId());
    }

    @Override
    public List<IotNode> listChildren(Long parentId) {
        return iotNodeMapper.findByParentId(parentId);
    }

    @Override
    @Transactional
    public void deleteNode(Long id) {
        iotNodeMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteDeviceNode(Long id){
        iotNodeMapper.updateDeviceNode(id);
    }
}
