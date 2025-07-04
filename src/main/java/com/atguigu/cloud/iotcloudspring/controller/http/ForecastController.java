package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DataPointDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DeviceDataDTO;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.atguigu.cloud.iotcloudspring.service.impl.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/IC")
@RequiredArgsConstructor
public class ForecastController {

    private final DeviceService deviceService;      // 你已有的
    private final ForecastService forecastService;  // 新增的

    private static final int WINDOW = 144;
    private static final List<String> KEYS = List.of("humidity","temperature","pm10","pm2_5");
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /** 返回下一时刻温度 */
    @GetMapping("/projects/{projectId}/forecast/nextTemp")
    public Result<Double> nextTemp(@PathVariable Long projectId) {

        /* ① 拉最近 144 个时间步 × 4 指标（共 576 行） */
        List<DeviceDataDTO> raw = deviceService.getLatestWindow(projectId, WINDOW);
        if (raw.size() < WINDOW * KEYS.size()) {
            return Result.error("数据不足，至少 "+ WINDOW +"×4 条");
        }

        /* ② 组装成 DataPointDTO 列表 */
        List<DataPointDTO> points = raw.stream()
                .map(d -> new DataPointDTO(
                        d.getTimestamp().format(ISO_FMT),
                        d.getDataKey(),
                        Double.parseDouble(d.getDataValue())))
                .collect(Collectors.toList());

        /* ③ 调用 FastAPI */
        double next = forecastService.forecastNextTemp(points);
        return Result.success(next);
    }
}
