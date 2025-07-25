package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.Device.BatchInferRequest;
import com.atguigu.cloud.iotcloudspring.DTO.Device.DataPointDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Device.InferResponseDto;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.BatchInferResponse;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.InferRequest;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.TrainRequest;
import com.atguigu.cloud.iotcloudspring.service.AnomalyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AnomalyServiceImpl implements AnomalyService {


    private final RestTemplate restTemplate;

    private String baseUrl;

    @Autowired
    public AnomalyServiceImpl(
            RestTemplate restTemplate,
            @Value("${anomaly.service.url}")
            String baseUrl
    ) {
        this.restTemplate = restTemplate;
        // 去掉末尾斜杠，保证拼 URL 时不出错
        this.baseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
    }

    @Override
    public void train(Long projectId, List<DataPointDTO> data) {
        String url = String.format("%s/train/%d", baseUrl, projectId);
        TrainRequest req = new TrainRequest();
        req.setData(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TrainRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<Void> resp = restTemplate.exchange(
                url, HttpMethod.POST, entity, Void.class
        );
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("训练失败: HTTP " + resp.getStatusCode());
        }
    }

    @Override
    public InferResponseDto infer(Long projectId, List<DataPointDTO> data) {
        String url = String.format("%s/infer/%d", baseUrl, projectId);
        InferRequest req = new InferRequest();
        req.setData(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<InferRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<InferResponseDto> resp = restTemplate.exchange(
                url, HttpMethod.POST, entity, InferResponseDto.class
        );
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("推理失败: HTTP " + resp.getStatusCode());
        }
        return resp.getBody();
    }

    @Override
    public BatchInferResponse batchInfer(Long projectId, List<List<DataPointDTO>> windows) {
        String url = String.format("%s/infer_batch/%d", baseUrl, projectId);
        BatchInferRequest req = new BatchInferRequest(windows);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BatchInferRequest> entity = new HttpEntity<>(req, headers);

        try {
            ResponseEntity<BatchInferResponse> resp = restTemplate.exchange(
                    url, HttpMethod.POST, entity, BatchInferResponse.class
            );
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new RuntimeException("批量推理失败，HTTP " + resp.getStatusCodeValue());
            }
            return resp.getBody();
        }
        // 捕获错误
        catch (RestClientException e) {
            log.warn("Anomaly 服务不可用，跳过孤立森林推理，返回默认空分数和正常标记", e);

            int n = windows.size();
            // 全部打 0 分，全部认为正常
            List<Double> defaultScores = Collections.nCopies(n, 0.0);
            List<Boolean> defaultAbnormals = Collections.nCopies(n, false);

            BatchInferResponse fallback = new BatchInferResponse();
            fallback.setScores(defaultScores);
            fallback.setAbnormals(defaultAbnormals);
            return fallback;
        }
    }
}
