package com.atguigu.cloud.iotcloudspring.IotNode.controller;

import com.atguigu.cloud.iotcloudspring.IotNode.pojo.IotNode;
import com.atguigu.cloud.iotcloudspring.IotNode.service.IotNodeService;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
@AllArgsConstructor
public class IotNodeController {

    private final IotNodeService nodeService;
    private final DeviceService deviceService;

    /**
     * 创建节点
     * POST /api/iot/node
     * {
     * "projectId": 1,
     * "parentId": null,      根节点时传 null
     * "nodeType": "柜子",
     * "nodeName": "A柜子",
     * "description": "第一层柜子",
     * "additionalInfo": "{}"
     * }
     */
    @PostMapping("/iot/node")
    public Result<IotNode> createNode(@RequestBody IotNode node) {
        IotNode created = nodeService.createNode(node);
        return Result.success(created);
    }

    /**
     * 获取某节点下所有直接子节点
     * GET /api/iot/node/children?parentId=123
     */
    @GetMapping("/children")
    public Result<List<IotNode>> listChildren(
            @RequestParam(value = "parentId", required = false
            ) Long parentId) {
        List<IotNode> children = nodeService.listChildren(parentId);
        return Result.success(children);
    }

    /**
     *  删除设备接口
     * del /iot/node/{id}
     */
    @DeleteMapping("/iot/node/{id}")
    public Result<Void> deleteNode(@PathVariable("id") Long id) {
        nodeService.deleteNode(id);
        return Result.success();
    }

    @PutMapping("/device/{id}/assignNode")
    public Result<Device> assignNode(
            @PathVariable("id") Long deviceId,
            @RequestParam("nodeId") Long nodeId) {
        Device updated = deviceService.assignNode(deviceId, nodeId);
        return Result.success(updated);
    }

    @GetMapping("/device/list/deviceNodes")
    public Result<List<Device>> listByNode(@RequestParam("nodeId") Long nodeId) {
        List<Device> devices = deviceService.listByNodeId(nodeId);
        return Result.success(devices);
    }

    // 删除设备节点的接口
    @PutMapping("/iot/deviceNode/{id}")
    public Result<Void> deleteDeviceNode(@PathVariable("id") Long id) {
        nodeService.deleteDeviceNode(id);
        return Result.success();
    }
}
