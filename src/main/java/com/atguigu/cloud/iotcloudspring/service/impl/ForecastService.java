package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DataPointDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ForecastService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ForecastService(RestTemplate restTemplate,
                           @Value("${anomaly.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    /** 调用 /forecast/next_temp，返回下一时刻温度 */
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
        String url = baseUrl + "/forecast/next_temp";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = Map.of("data", window);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            Object v = resp.getBody().get("next_temperature");
            return v == null ? Double.NaN : ((Number) v).doubleValue();
        }
        throw new RuntimeException("预测失败 HTTP " + resp.getStatusCodeValue());
    }

    /** 批量预测（可选） */
    public List<Double> forecastBatch(List<List<DataPointDTO>> windows) {
        String url = baseUrl + "/forecast_batch/next_temp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = Map.of("data", windows);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            // FastAPI 返回 {"next_temperatures":[...]}
            return ((List<?>) resp.getBody().get("next_temperatures"))
                    .stream().map(o -> ((Number) o).doubleValue()).toList();
        }
        throw new RuntimeException("批量预测失败 HTTP " + resp.getStatusCodeValue());
    }
}