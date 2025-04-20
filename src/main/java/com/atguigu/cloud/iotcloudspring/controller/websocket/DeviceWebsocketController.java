package com.atguigu.cloud.iotcloudspring.controller.websocket;

import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceDetailResponse;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import jakarta.annotation.Resource;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceWebsocketController {
    @Resource
    private DeviceService deviceService;

    @MessageMapping("/detail/{id}")
    @SendTo("/topic/detail/{id}")
    public Result<DeviceDetailResponse> getDeviceDetail(@DestinationVariable Long id) {
        DeviceDetailResponse response = deviceService.getDeviceDetail(id);
        return Result.success(response);
    }
}
