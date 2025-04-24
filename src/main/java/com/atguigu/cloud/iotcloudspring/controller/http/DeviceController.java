package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeAttributeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceTypeDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.*;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.FieldTemplate;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.atguigu.cloud.iotcloudspring.service.FieldTemplateService;
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
    private FieldTemplateService fieldTemplateService;

    @Resource
    private UserService userService;

    //创建设备类型
    @PostMapping("/create")
    public Result<Void> createDeviceType(@RequestBody DeviceTypeDTO deviceTypeDTO,
                                         Authentication authentication) {
        //  从 Authentication 中获取当前登录用户的用户名
        String username = (String) authentication.getPrincipal();
        //  根据用户名查询出用户 ID
        Long userId = userService.findUserIdByUsername(username);

        // 验证当前用户是否有权限操作该项目(后面再用)
        // if (!projectService.checkUserOwnsProject(userId, deviceTypeDTO.getProjectId())) {
        //     return Result.error("无权限操作该项目");
        // }

        //  调用 service 层进行保存
        deviceService.createDeviceType(deviceTypeDTO);

        return Result.success();
    }

    //删除设备类型
    @DeleteMapping("/deleteType/{id}")
    public Result<Void> deleteDeviceType(@PathVariable Long id) {
        boolean success = deviceService.deleteDeviceType(id);
        if (success) {
            return Result.success();
        }else {
            return Result.error("删除失败");
        }
    }

    @GetMapping("/deviceType/{projectid}")
    public Result<List<DeviceTypeResponse>> getDeviceTypeList(@PathVariable Long projectid) {
        List<DeviceTypeResponse> list = deviceService.getDeviceTypeList(projectid);
        return Result.success(list);
    }

    @GetMapping("/deviceTypeName/{projectid}")
    public Result<List<DeviceTypeNameResponse>> getDeviceTypeById(@PathVariable Long projectid) {
        List<DeviceTypeNameResponse> list = deviceService.getDeviceTypeNameList(projectid);
        return Result.success(list);
    }

    @PostMapping("/createTypeAttribute")
    public Result<Long> createDeviceTypeAttribute(@RequestBody DeviceTypeAttributeDTO attributeDTO) {
        Long id = deviceService.createDeviceTypeAttribute(attributeDTO);
        return Result.success(id);
    }

    // 根据设备类型ID查询属性列表
    @GetMapping("/deviceAttributeType/{devicetypeid}")
    public Result<List<DeviceTypeAttributeResponse>> getAttributesByDeviceTypeId(@PathVariable Long devicetypeid) {
        List<DeviceTypeAttributeResponse> list = deviceService.getAttributesByDeviceTypeId(devicetypeid);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDeviceTypeAttribute(@PathVariable Long id) {
        boolean success = deviceService.deleteDeviceTypeAttribute(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    // 创建设备
    @PostMapping("/createDevice")
    public Result<Long> createDevice(@RequestBody DeviceDTO deviceDTO) {
        Long id = deviceService.createDevice(deviceDTO);
        return Result.success(id);
    }

    //获取设备id获取设备名称
    @GetMapping("/deviceName/{id}")
    public Result<String> getDeviceName(@PathVariable Long id) {
        String deviceName = deviceService.getDeviceName(id);
        return Result.success(deviceName);
    }

    //删除设备
    @DeleteMapping("/deleteDevice/{id}")
    public Result<Void> deleteDevice(@PathVariable Long id) {
        boolean success = deviceService.deleteDevice(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @GetMapping("/detail/{id}")
    public Result<DeviceDetailResponse> getDeviceDetail(@PathVariable Long id) {
        DeviceDetailResponse response = deviceService.getDeviceDetail(id);
        return Result.success(response);
    }

    @GetMapping("/detail/project/{projectid}")
    public Result<List<DeviceDetailResponse>> getDeviceDetailsByProject(@PathVariable Long projectid) {
        List<DeviceDetailResponse> responses = deviceService.getDeviceDetailsByProject(projectid);
        return Result.success(responses);
    }

    //查询关联的设备
    @GetMapping("/connectedDevices/{devicetypeid}")
    public Result<List<DeviceConnectResponse>> getConnectedDevices(@PathVariable Long devicetypeid) {
        List<DeviceConnectResponse> responses = deviceService.getConnectedDevices(devicetypeid);
        return Result.success(responses);
    }

    //根据设备类型id查询设备类型名称
    @GetMapping("/deviceType/deviceTypeName/{id}")
    public Result<String> getDeviceTypeName(@PathVariable Long id) {
        String deviceTypeName = deviceService.getDeviceTypeName(id);
        return Result.success(deviceTypeName);
    }

    @GetMapping("/fieldTemplates")
    public Result<List<FieldTemplate>> listAll() {
        List<FieldTemplate> list = fieldTemplateService.listAll();
        return Result.success(list);
    }
}
