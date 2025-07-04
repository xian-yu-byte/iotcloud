package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Device.*;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.*;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.FieldTemplate;
import com.atguigu.cloud.iotcloudspring.service.AnomalyService;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.atguigu.cloud.iotcloudspring.service.FieldTemplateService;
import com.atguigu.cloud.iotcloudspring.service.UserService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @Resource
    private FieldTemplateService fieldTemplateService;

    @Resource
    private UserService userService;

    @Resource
    private AnomalyService anomalyService;

    private final SimpMessagingTemplate template;

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

    // 根据设备id获取设备关联设备类型数据
    @GetMapping("/{id}/attributes")
    public Result<List<DeviceIDName>> getAttributes(@PathVariable("id") Long deviceId) {
        List<DeviceIDName> attributes = deviceService.getAttributeNamesByDeviceId(deviceId);
        return Result.success(attributes);
    }

    //根据设备类型id查询设备类型名称
    @GetMapping("/deviceType/deviceTypeName/{id}")
    public Result<String> getDeviceTypeName(@PathVariable Long id) {
        String deviceTypeName = deviceService.getDeviceTypeName(id);
        return Result.success(deviceTypeName);
    }

    @GetMapping("/{projectId}/fieldTemplates")
    public Result<List<FieldTemplate>> listAll(@PathVariable("projectId") Long projectId) {
        List<FieldTemplate> list = fieldTemplateService.listAll(projectId);
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

    @PostMapping("/insert/template")
    public Result<Long> setTemplate(@RequestBody FieldTemplate template) {

        Long created = deviceService.setTemplate(template);
        return Result.success(created);
    }


    /**
     * 健壮性很高的一个通用接口，可以根据deviceKey+fieldKey来查，也可以通过deviceId+devicetypeattributeId来查
     */
    @GetMapping("/device/history")
    public Result<List<DeviceAttributePointDTO>> getHistory(
            @RequestParam Long projectId,
            @RequestParam(required = false) String deviceKey,
            @RequestParam(required = false) String fieldKey,
            @RequestParam(required = false) Long deviceId,
            @RequestParam(required = false) Long attributeId,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime
    ) {
        // 拉历史原始数据
        List<DeviceAttributePointDTO> history = (deviceId != null && attributeId != null)
                ? deviceService.fetchHistoryById(deviceId, attributeId, days, startTime, endTime)
                : deviceService.fetchHistoryByKey(deviceKey, fieldKey, days, startTime, endTime);

        if (history.isEmpty()) {
            return Result.error("无历史数据");
        }

        // 构造滑窗批量请求
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        List<List<DataPointDTO>> windows = new ArrayList<>(history.size());
        for (int i = 0; i < history.size(); i++) {
            int start = Math.max(0, i - 9);
            List<DataPointDTO> w = history.subList(start, i + 1).stream()
                    .map(d -> {
                        // 这里用 fieldKey 作为 datakey
                        double v = d.getDatavalue() != null
                                ? d.getDatavalue()
                                : 0.0;
                        String key = fieldKey != null ? fieldKey : history.getFirst().getDatakey();
                        return new DataPointDTO(
                                d.getTimestamp().format(fmt),
                                key,
                                v
                        );
                    })
                    .collect(Collectors.toList());
            windows.add(w);
        }

        // 批量推理
        BatchInferResponse batch = anomalyService.batchInfer(projectId, windows);
        List<Double> scores = batch.getScores();
        List<Boolean> abns = batch.getAbnormals();

        // 拼回 DTO
        for (int i = 0; i < history.size(); i++) {
            history.get(i).setAnomalyScore(scores.get(i));
            history.get(i).setAbnormal(abns.get(i));
        }

        return Result.success(history);
    }

    @GetMapping("/device/{projectId}/all")
    public Result<String> getAllDevices(@PathVariable Long projectId) {
        return Result.success();
    }

    @GetMapping("/{deviceId}/counts")
    public Result<List<MessageCountDTO>> getMessageCounts(
            @PathVariable Long deviceId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime,
            @RequestParam(defaultValue = "HOUR") String interval,
            @RequestParam(required = false) Integer days
    ) {
        if (startTime == null || endTime == null) {
            endTime = LocalDateTime.now();
            startTime = endTime.minusDays(days != null ? days : 1);
        }
        List<MessageCountDTO> list = deviceService.getMessageCounts(deviceId, startTime, endTime, interval);
        return Result.success(list);
    }

    @GetMapping("/{deviceId}/latency")
    public Result<List<MessageLatencyDTO>> getMessageLatency(
            @PathVariable Long deviceId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime,
            @RequestParam(defaultValue = "HOUR") String interval,
            @RequestParam(required = false) Integer days
    ) {
        if (startTime == null || endTime == null) {
            endTime = LocalDateTime.now();
            startTime = endTime.minusDays(days != null ? days : 1);
        }
        List<MessageLatencyDTO> list = deviceService.getMessageLatency(deviceId, startTime, endTime, interval);
        return Result.success(list);
    }

    @GetMapping("/projects/{projectId}/data")
    public Result<List<DeviceDataDTO>> getProjectData(
            @PathVariable Long projectId) {
        List<DeviceDataDTO> list = deviceService.getDataByProjectId(projectId);
        if (list.isEmpty()) {
            return Result.error("数据不足");
        }
        return Result.success(list);
    }

    /**
     * 手动触发重训（应急）
     */
    @PostMapping("/projects/{projectId}/anomaly/train")
    public Result<Void> train(@PathVariable Long projectId) {
        // 这里也可以让前端传历史数据，否则就复用 /data
        List<DeviceDataDTO> list = deviceService.getDataByProjectId(projectId);
        if (list.size() < 20) {
            return Result.error("训练数据不足，至少20条");
        }
        // 转成 Python 那边的 DataPointDTO
        DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // 或自定义 "yyyy-MM-dd HH:mm:ss"
        List<DataPointDTO> pts = list.stream()
                .map(d -> {
                    try {
                        double v = Double.parseDouble(d.getDataValue());
                        String isoTs = d.getTimestamp().format(ISO_FMT);   // ← 关键
                        return new DataPointDTO(isoTs, d.getDataKey(), v);
                    } catch (NumberFormatException ex) {
                        log.warn("跳过非法 datavalue='{}'  recordId={}", d.getDataValue(), d.getDeviceId());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3. 再次判断条数，保证模型训练够数据
        if (pts.size() < 20) {
            return Result.error("可用训练数据不足，至少 20 条（过滤非法值后）");
        }
        anomalyService.train(projectId, pts);
        return Result.success();
    }

    /**
     * 手动或前端轮询调用推理
     */
    @PostMapping("/projects/{projectId}/anomaly/infer")
    public Result<InferResponseDto> infer(@PathVariable Long projectId) {
        List<DeviceDataDTO> list = deviceService.getDataByProjectId(projectId);
        if (list.size() < 5) {
            return Result.error("时序数据不足，至少5条");
        }
        DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        List<DataPointDTO> pts = list.stream()
                .map(d -> new DataPointDTO(d.getTimestamp().format(ISO_FMT), d.getDataKey(),
                        Double.parseDouble(d.getDataValue())))
                .collect(Collectors.toList());
        InferResponseDto resp = anomalyService.infer(projectId, pts);
        return Result.success(resp);
    }

    //websocket实时获得数据改为http轮询获得数据接口
    @GetMapping("/app/detail/{id}")
    public Result<DeviceDetailResponse> getDeviceHttpDetail(@PathVariable Long id) {
        DeviceDetailResponse response = deviceService.getDeviceDetail(id);
        return Result.success(response);
    }

    // 黄rb的接收接口
    @PostMapping("/control")
    public Result<Void> control(@RequestBody Map<String, Object> payload) {
        // 1) 可以在这里做业务校验/日志……
        // 2) 直接把整个 Map 广播给所有订阅者
        template.convertAndSend("/topic/device/state", payload);
        return Result.success();
    }
}
