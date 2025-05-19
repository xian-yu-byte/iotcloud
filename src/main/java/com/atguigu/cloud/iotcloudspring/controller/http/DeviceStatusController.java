package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Device.ProjectDeviceStatsDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceStatusResponse;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.service.DeviceStatusService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class DeviceStatusController {

    @Resource
    private DeviceStatusService deviceStatusService;

    @PostMapping("/update")
    public ResponseEntity<Result<DeviceStatusResponse>> updateDeviceStatus(@RequestBody DeviceStatusResponse dto) {
        DeviceStatusResponse deviceStatusResponse = deviceStatusService.updateOrInsertDeviceStatus(dto.getId(), dto.getDevicestatus());
        return ResponseEntity.ok(Result.success(deviceStatusResponse));
    }

    @GetMapping("/{projectId}/deviceStats")
    public Result<ProjectDeviceStatsDTO> getDeviceStats(@PathVariable Long projectId) {
        ProjectDeviceStatsDTO stats = deviceStatusService.getProjectStats(projectId);
        return Result.success(stats);
    }
}
