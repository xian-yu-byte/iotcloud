package com.atguigu.cloud.iotcloudspring.WeChat.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.cloud.iotcloudspring.WeChat.config.WeChatConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeChatMessageService {

    private final WeChatConfig weChatConfig;
    private final WeChatTokenService tokenService;
    private final RestTemplate restTemplate;

    // ----------- 对外唯一入口 -----------
    public void sendAlarmTemplate(String openid,
                                  String projectName,   // 未来可放到 data 里
                                  String deviceName,
                                  String alarmReason,
                                  String deviceId,
                                  String triggerTime,  // 目前未用，可替换 LocalDateTime.now()
                                  String alertLevel,
                                  Long eventId) {

        JSONObject data = new JSONObject()
                .fluentPut("thing8", new JSONObject().fluentPut("value",
                        Objects.requireNonNullElse(deviceName, "-")))
                .fluentPut("character_string13", new JSONObject().fluentPut("value", deviceId))
                .fluentPut("time5", new JSONObject().fluentPut("value",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .fluentPut("thing6", new JSONObject().fluentPut("value", alarmReason))
                .fluentPut("character_string12", new JSONObject().fluentPut("value", alertLevel));

        JSONObject payload = new JSONObject()
                .fluentPut("touser", openid)
                .fluentPut("template_id", weChatConfig.getAlarmTemplateId())
                .fluentPut("url", "https://iotclouds.top/alarm-details?id=" + eventId)
                .fluentPut("data", data);

        sendWithTokenRetry(payload);
    }

    // ---------- 统一发送 + 自动重试 ----------
    private static final Set<Integer> NEED_REFRESH_CODES = Set.of(40001, 40014, 42001);

    private void sendWithTokenRetry(JSONObject payload) {

        // 第一次
        String resp = doSend(payload, tokenService.getTokenSafely(), false);
        Integer code = parseErrCode(resp);
        if (code == null || code == 0) return;      // 发送成功

        // 需要刷新并重试
        if (NEED_REFRESH_CODES.contains(code)) {
            log.warn("token 失效(errcode={})，刷新并重发", code);
            String newToken = tokenService.refreshToken(true);  // force = true
            resp = doSend(payload, newToken, true);
            code = parseErrCode(resp);
        }

        // 仍失败则记录
        if (code != null && code != 0) {
            log.error("模板消息发送失败(errcode={})，resp={}", code, resp);
        }
    }

    /**
     * 真正发 HTTP 请求；异常→null
     */
    private String doSend(JSONObject payload, String token, boolean retry) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
        try {
            String resp = restTemplate.postForObject(url, payload.toJSONString(), String.class);
            log.info("模板消息{}返回：{}", retry ? "重试" : "第一次", resp);
            return resp;
        } catch (Exception e) {
            log.error("调用微信模板接口{}发生异常", retry ? "（重试）" : "", e);
            return null;
        }
    }

    /**
     * 解析 errcode（null 表示无 / 解析失败）
     */
    private Integer parseErrCode(String resp) {
        try {
            return JSONObject.parseObject(resp).getInteger("errcode");
        } catch (Exception ignore) {
            return null;
        }
    }
}

