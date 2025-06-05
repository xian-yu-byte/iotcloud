package com.atguigu.cloud.iotcloudspring.WeChat.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.cloud.iotcloudspring.WeChat.config.WeChatConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeChatMessageService {
    private final WeChatConfig weChatConfig;
    private final WeChatTokenService weChatTokenService;
    private final RestTemplate restTemplate;

    /**
     * 发送告警模板消息
     *
     * @param openid      用户openid
     * @param projectName 项目名称
     * @param deviceName  设备名称
     * @param triggerTime 触发时间（格式化字符串）
     * @param alertLevel  告警等级（如“紧急”）
     */
    public void sendAlarmTemplate(String openid,
                                  String projectName,
                                  String deviceName,
                                  String alarmReason,
                                  String deviceId,
                                  String triggerTime,
                                  String alertLevel,
                                  Long eventId) {
        // 1. 组装 data 部分，必须保证每个字段非空
        JSONObject data = new JSONObject();
        data.put("thing8", new JSONObject().fluentPut("value", Objects.requireNonNullElse(deviceName, "-")));
        data.put("character_string13", new JSONObject().fluentPut("value", deviceId));
        data.put("time5", new JSONObject().fluentPut("value", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        data.put("thing6", new JSONObject().fluentPut("value", alarmReason));
        data.put("character_string12", new JSONObject().fluentPut("value", alertLevel));

        // 2. 组装完整 payload
        JSONObject payload = new JSONObject();
        payload.put("touser", openid);
        payload.put("template_id", weChatConfig.getAlarmTemplateId());
        // 点击消息后跳转到告警详情页
        payload.put("url", "https://iotclouds.top/alarm-details?id=" + eventId);
        payload.put("data", data);

        sendWithTokenRetry(payload);

        // 3. 调用微信模板消息接口
//        String accessToken = weChatTokenService.getAccessToken();
//        String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
//        String resp = restTemplate.postForObject(sendUrl, payload.toJSONString(), String.class);

        // 4. （可选）打印返回值以便排查
//        log.info("微信模板消息发送返回：{}", resp);
    }

    private void sendWithTokenRetry(JSONObject payload) {
        log.info("▶ 准备调用微信模板 API，payload = {}", payload.toJSONString());
        // 第一次尝试
        String accessToken = weChatTokenService.getAccessToken();
        String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;

        String resp = null;
        try {
            resp = restTemplate.postForObject(sendUrl, payload.toJSONString(), String.class);
        } catch (Exception ex) {
            log.error("调用微信模板消息接口时发生异常，准备刷新 token 并重试。", ex);
        }
        log.info("微信模板消息第一次返回：{}", resp);

        // 如果微信返回 errcode=40001，说明 access_token 已失效，需要刷新并重试
        if (resp != null) {
            JSONObject jsonResp = JSONObject.parseObject(resp);
            Integer errcode = jsonResp.getInteger("errcode");
            if (errcode != null && errcode == 40001) {
                log.warn("access_token 失效（errcode=40001），开始刷新 token 并重发。原返回：{}", resp);
                // 刷新 token
                weChatTokenService.refreshToken();
                // 取新的 token，再次发送
                String newToken = weChatTokenService.getAccessToken();
                String retryUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + newToken;
                try {
                    String retryResp = restTemplate.postForObject(retryUrl, payload.toJSONString(), String.class);
                    restTemplate.getMessageConverters().stream()
                            .filter(c -> c instanceof StringHttpMessageConverter)
                            .map(c -> (StringHttpMessageConverter) c)
                            .forEach(c ->
                                    log.info("➡️  StringHttpMessageConverter = {}, charset = {}",
                                            c, c.getDefaultCharset()));
                    log.info("微信模板消息重试返回：{}", retryResp);
                } catch (Exception retryEx) {
                    log.error("刷新 token 后重试发送模板消息时发生异常。", retryEx);
                }
            }
        }
    }
}
