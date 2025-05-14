package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceGroup;
import com.atguigu.cloud.iotcloudspring.service.DeviceGroupService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC/deviceGroup")
@CrossOrigin("*")
public class DeviceGroupController {

    @Resource
    private DeviceGroupService deviceGroupService;

    // 获取设备组列表
    @GetMapping("/list")
    public Result<List<DeviceGroup>> getDeviceGroupList(
            @RequestParam Long projectId) {
        List<DeviceGroup> deviceGroups = deviceGroupService.getDeviceGroupList(projectId);
        return Result.success(deviceGroups);
    }

    // 创建设备组
    @PostMapping("/create")
    public Result<Void> createDeviceGroup(
            @RequestBody DeviceGroup deviceGroup) {
        deviceGroupService.createDeviceGroup(deviceGroup);
        return Result.success();
    }

    // 获取设备组所有设备列表
    @GetMapping("/deviceList")
    public Result<List<Device>> getDeviceList(
            @RequestParam Long groupId){
        List<Device> devices = deviceGroupService.getDeviceList(groupId);
        return Result.success(devices);
    }

    // 更新设备组
    @PutMapping("/update")
    public Result<Void> updateDeviceGroup(
            @RequestBody DeviceGroup newDeviceGroup){
        Boolean success = deviceGroupService.updateDeviceGroup(newDeviceGroup);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    // 删除设备组
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDeviceGroup(@PathVariable Long id){
        Boolean success = deviceGroupService.deleteDeviceGroup(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    // 添加设备到设备组
    @PutMapping("/addDevice")
    public Result<Void> addDeviceToGroup(
            @RequestParam Long groupId, @RequestParam Long deviceId){
        deviceGroupService.addDeviceToGroup(groupId, deviceId);
        return Result.success();
    }

    // 从设备组移除设备
    @PutMapping("/removeDevice")
    public Result<Void> removeDeviceFromGroup(
            @RequestParam Long groupId, @RequestParam Long deviceId){
        Boolean success = deviceGroupService
                .removeDeviceFromGroup(groupId, deviceId);
        return success ? Result.success() : Result.error("移除失败");
    }
}
