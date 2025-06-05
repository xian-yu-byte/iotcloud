package com.atguigu.cloud.iotcloudspring.WeChat.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.cloud.iotcloudspring.WeChat.config.WeChatConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeChatTokenService {
    private final WeChatConfig config;
    private final RestTemplate rest = new RestTemplate();

    @Getter
    private volatile String accessToken;

    @PostConstruct
    public void init() {
        refreshToken();
    }

    /** 应用启动后立即去微信拉一次 */
    @Scheduled(initialDelay = 1000, fixedDelay = 7000_000) // 每隔 7000 秒刷新一次
    public void refreshToken() {
        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/token?" +
                        "grant_type=client_credential&appid=%s&secret=%s",
                config.getAppId(), config.getSecret()
        );
        String resp = rest.getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(resp);
        this.accessToken = json.getString("access_token");
        // 后续考虑加上失败重试、日志等
    }
}
