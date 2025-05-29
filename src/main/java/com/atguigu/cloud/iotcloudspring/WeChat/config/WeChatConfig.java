package com.atguigu.cloud.iotcloudspring.WeChat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class WeChatConfig {

    /**
     * 微信服务号的 AppID（在公众号后台 → 设置 → 公众号设置 中获取）
     */
    @Value("${wechat.appid}")
    private String appId;

    /**
     * 微信服务号的 AppSecret（在公众号后台 → 设置 → 公众号设置 中获取）
     */
    @Value("${wechat.secret}")
    private String secret;

    /**
     * 在“模板消息”里配置的告警模板 ID
     */
    @Value("${wechat.template-id.alarm}")
    private String alarmTemplateId;
}
