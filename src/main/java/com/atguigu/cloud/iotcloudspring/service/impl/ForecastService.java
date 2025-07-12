package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DataPointDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ForecastService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private static final int NULL_THRESHOLD = 3;
    private final AtomicInteger nullStreak = new AtomicInteger(0);

    public ForecastService(RestTemplate restTemplate,
                           @Value("${anomaly.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    /**
     * 调用 /forecast/next_temp，返回下一时刻温度
     */
    public double forecastNextTemp(List<DataPointDTO> window) {
        log.info("window size = {}", window.size());

        Map<String, Long> stat = window.stream()
                .collect(Collectors.groupingBy(DataPointDTO::getDatakey, Collectors.counting()));
        log.info("key count per window = {}", stat);          // 应各 =144

        window.stream()
                .collect(Collectors.groupingBy(DataPointDTO::getTimestamp))
                .entrySet().stream()
                .filter(e -> e.getValue().size() < 4)             // 缺键的时间戳
                .limit(10)
                .forEach(e -> log.warn("缺键 {} -> {}", e.getKey(), e.getValue()));
        if (nullStreak.get() >= NULL_THRESHOLD) {
            log.warn("已连续 {} 次空结果，暂停调用预测服务", NULL_THRESHOLD);
            return Double.NaN;
        }
        String url = baseUrl + "/forecast/next_temp";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = Map.of("data", window);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            Object v = resp.getBody().get("next_temperature");
            if (v == null) {
                int streak = nullStreak.incrementAndGet();
                log.warn("预测结果为 null，当前连击 = {}", streak);
                return Double.NaN;
            } else {
                nullStreak.set(0);
                return ((Number) v).doubleValue();
            }
        }
        throw new RuntimeException("预测失败 HTTP " + resp.getStatusCode());
    }

    /**
     * 批量预测（可选）
     */
    public List<Double> forecastBatch(List<List<DataPointDTO>> windows) {

        if (nullStreak.get() >= NULL_THRESHOLD) {
            log.warn("已连续 {} 次空结果，暂停批量预测调用", NULL_THRESHOLD);
            // 这里直接返回全 NaN，或者返回空列表，看业务需要
            return windows.stream().map(w -> Double.NaN).toList();
        }

        String url = baseUrl + "/forecast_batch/next_temp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(Map.of("data", windows), headers);

        ResponseEntity<Map> resp =
                restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            List<?> arr = (List<?>) resp.getBody().get("next_temperatures");
            boolean anyNull = false;
            List<Double> res = new ArrayList<>(arr.size());
            for (Object o : arr) {
                if (o == null) anyNull = true;
                res.add(o == null ? Double.NaN : ((Number) o).doubleValue());
            }
            // ★ 批量里只要出现 null，就算一次“空结果”
            if (anyNull) {
                int streak = nullStreak.incrementAndGet();
                log.warn("批量预测出现 null，当前连击 = {}", streak);
            } else {
                nullStreak.set(0);
            }
            return res;
        }
        throw new RuntimeException("批量预测失败 HTTP " + resp.getStatusCode());
    }
}