package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeAttributeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.*;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.atguigu.cloud.iotcloudspring.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @Resource
    private UserService userService;

    @PostMapping("/create")
    public Result<Void> createDeviceType(@RequestBody DeviceTypeDTO deviceTypeDTO,
                                         Authentication authentication) {
        //  从 Authentication 中获取当前登录用户的用户名
        String username = (String) authentication.getPrincipal();
        //  根据用户名查询出用户 ID
        Integer userId = userService.findUserIdByUsername(username);

        // 验证当前用户是否有权限操作该项目(后面再用)
        // if (!projectService.checkUserOwnsProject(userId, deviceTypeDTO.getProjectId())) {
        //     return Result.error("无权限操作该项目");
        // }

        //  调用 service 层进行保存
        deviceService.createDeviceType(deviceTypeDTO);

        return Result.success();
    }

    @GetMapping("/deviceType/{projectid}")
    public Result<List<DeviceTypeResponse>> getDeviceTypeList(@PathVariable Integer projectid) {
        List<DeviceTypeResponse> list = deviceService.getDeviceTypeList(projectid);
        return Result.success(list);
    }

    @GetMapping("/deviceTypeName/{projectid}")
    public Result<List<DeviceTypeNameResponse>> getDeviceTypeById(@PathVariable Integer projectid) {
        List<DeviceTypeNameResponse> list = deviceService.getDeviceTypeNameList(projectid);
        return Result.success(list);
    }

    @PostMapping("/createTypeAttribute")
    public Result<Integer> createDeviceTypeAttribute(@RequestBody DeviceTypeAttributeDTO attributeDTO) {
        Integer id = deviceService.createDeviceTypeAttribute(attributeDTO);
        return Result.success(id);
    }

    // 根据设备类型ID查询属性列表
    @GetMapping("/deviceAttributeType/{devicetypeid}")
    public Result<List<DeviceTypeAttributeResponse>> getAttributesByDeviceTypeId(@PathVariable Integer devicetypeid) {
        List<DeviceTypeAttributeResponse> list = deviceService.getAttributesByDeviceTypeId(devicetypeid);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDeviceTypeAttribute(@PathVariable Integer id) {
        boolean success = deviceService.deleteDeviceTypeAttribute(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    // 创建设备
    @PostMapping("/createDevice")
    public Result<Integer> createDevice(@RequestBody DeviceDTO deviceDTO) {
        Integer id = deviceService.createDevice(deviceDTO);
        return Result.success(id);
    }

    @GetMapping("/detail/{id}")
    public Result<DeviceDetailResponse> getDeviceDetail(@PathVariable Integer id) {
        DeviceDetailResponse response = deviceService.getDeviceDetail(id);
        return Result.success(response);
    }

    @GetMapping("/detail/project/{projectid}")
    public Result<List<DeviceDetailResponse>> getDeviceDetailsByProject(@PathVariable Integer projectid) {
        List<DeviceDetailResponse> responses = deviceService.getDeviceDetailsByProject(projectid);
        return Result.success(responses);
    }
}
