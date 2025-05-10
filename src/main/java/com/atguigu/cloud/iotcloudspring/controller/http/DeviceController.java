package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceAttributePointDTO;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    //根据id查询设备类型
    @GetMapping("/deviceType/detail/{typeId}")
    public Result<DeviceTypeDTO> getDeviceTypeDetailById(@PathVariable Long typeId) {
        DeviceTypeDTO deviceTypeDTO = deviceService.getDeviceTypeDetailById(typeId);
        return Result.success(deviceTypeDTO);
    }

    //更新设备类型
    @PutMapping("/updateDeviceType")
    public Result<Void> updateDeviceType(@RequestBody DeviceTypeDTO deviceTypeDTO) {
        boolean success = deviceService.updateDeviceType(deviceTypeDTO);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    //删除设备类型
    @DeleteMapping("/deleteType/{id}")
    public Result<Void> deleteDeviceType(@PathVariable Long id) {
        boolean success = deviceService.deleteDeviceType(id);
        if (success) {
            return Result.success();
        } else {
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

    // 根据ID获取属性字段
    @GetMapping("/deviceAttribute/{id}")
    public Result<DeviceTypeAttributeResponse> getDeviceAttributeById(@PathVariable Long id) {
        DeviceTypeAttributeResponse response = deviceService.getDeviceAttributeById(id);
        return Result.success(response);
    }

    // 更新属性
    @PutMapping("/updateDeviceAttribute")
    public Result<Void> updateDeviceAttribute(@RequestBody DeviceTypeAttributeDTO attributeDTO) {
        boolean success = deviceService.updateDeviceAttribute(attributeDTO);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
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

    // 更新设备
    @PutMapping("/updateDevice")
    public Result<Void> updateDevice(@RequestBody DeviceDTO deviceDTO) {
        boolean success = deviceService.updateDevice(deviceDTO);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
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

    @GetMapping("/deviceTypeName")
    public Result<Long> getDeviceNameList(String deviceTypeName) {
        long deviceTypeId = deviceService.getDeviceTypeId(deviceTypeName);
        return Result.success(deviceTypeId);
    }

    /**
     * 查询指定设备某个属性的最新值
     * GET /IC/device/{devicekey}/property/{prop}
     */
    @GetMapping("/device/{devicekey}/property/{fieldkey}")
    public Result<String> getProperty(
            @PathVariable String devicekey,
            @PathVariable String fieldkey) {
        String value = deviceService.getLatestData(devicekey, fieldkey);
        if (value != null) {
            return Result.success(value);
        } else {
            return Result.error("未查询到对应数据");
        }
    }

    @GetMapping("/device/{devicekey}/properties")
    public Result<Map<String, String>> getProperties(
            @PathVariable String devicekey,
            @RequestParam List<String> fieldKeys) {
        System.out.println("fieldKeys = " + fieldKeys);
        Map<String, String> values = deviceService.getLatestDatas(devicekey, fieldKeys);
        return Result.success(values);
    }

    /**
     * 健壮性很高的一个通用接口，可以根据deviceKey+fieldKey来查，也可以通过deviceId+devicetypeattributeId来查
     */
    @GetMapping("/device/history")
    public Result<List<DeviceAttributePointDTO>> getHistory(
            // 方式 A：通过 Key 查
            @RequestParam(required = false) String deviceKey,
            @RequestParam(required = false) String fieldKey,
            // 方式 B：直接拿 ID
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) Long attributeId,
            // 通用的时间参数
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime
    ) {
        if (deviceId != null && attributeId != null) {
            // 用数字 ID 查询
            return Result.success(
                    deviceService.fetchHistoryById(deviceId, attributeId,
                            days, startTime, endTime)
            );
        } else {
            // 用 key 查询
            return Result.success(
                    deviceService.fetchHistoryByKey(deviceKey, fieldKey,
                            days, startTime, endTime)
            );
        }
    }
}
