package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.Common.utils.NetworkUtil;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AiDevice.DeviceReportReqDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AiDevice.DeviceReportRespDTO;
import com.atguigu.cloud.iotcloudspring.service.AiDeviceService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@Tag(name = "设备管理", description = "OTA 相关接口")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/ota/")
public class OTAController {
    private final AiDeviceService aiDeviceService;

    @Operation(summary = "检查 OTA 版本和设备激活状态")
    @PostMapping
    public ResponseEntity<String> checkOTAVersion(
            @RequestBody DeviceReportReqDTO deviceReportReqDTO,
            @Parameter(name = "Device-Id", description = "设备唯一标识", required = true, in = ParameterIn.HEADER) @RequestHeader("Device-Id") String deviceId,
            @Parameter(name = "Client-Id", description = "客户端标识", required = false, in = ParameterIn.HEADER) @RequestHeader(value = "Client-Id", required = false) String clientId) {
        if (StringUtils.isBlank(deviceId)) {
            return createResponse(DeviceReportRespDTO.createError("Device ID is required"));
        }
        if (StringUtils.isBlank(clientId)) {
            clientId = deviceId;
        }
        String macAddress = deviceReportReqDTO.getMacAddress();
        boolean macAddressValid = NetworkUtil.isMacAddressValid(macAddress);
        // 设备Id和Mac地址应是一致的, 并且必须需要application字段
        if (!deviceId.equals(macAddress) || !macAddressValid || deviceReportReqDTO.getApplication() == null) {
            return createResponse(DeviceReportRespDTO.createError("Invalid OTA request"));
        }
        return createResponse(aiDeviceService.checkDeviceActive(macAddress, clientId, deviceReportReqDTO));
    }

    @GetMapping
    public ResponseEntity<String> getOTAPrompt() {
        return createResponse(DeviceReportRespDTO.createError("请提交正确的ota参数"));
    }

    @SneakyThrows
    private ResponseEntity<String> createResponse(DeviceReportRespDTO deviceReportRespDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = objectMapper.writeValueAsString(deviceReportRespDTO);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(jsonBytes.length)
                .body(json);
    }
}
