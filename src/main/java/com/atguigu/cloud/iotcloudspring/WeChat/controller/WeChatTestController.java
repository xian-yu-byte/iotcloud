package com.atguigu.cloud.iotcloudspring.WeChat.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.cloud.iotcloudspring.WeChat.config.WeChatConfig;
import com.atguigu.cloud.iotcloudspring.WeChat.mapper.UserOpenidMapper;
import com.atguigu.cloud.iotcloudspring.WeChat.pojo.UserOpenid;
import com.atguigu.cloud.iotcloudspring.WeChat.service.Impl.WeChatMessageService;
import com.atguigu.cloud.iotcloudspring.WeChat.service.Impl.WeChatTokenService;
import com.atguigu.cloud.iotcloudspring.WeChat.service.Impl.WeChatUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/wechat")
@RequiredArgsConstructor
public class WeChatTestController {
    private final WeChatTokenService tokenService;
    private final UserOpenidMapper openidMapper;
    private final WeChatUserService userService;
    private final WeChatMessageService messageService;
    private final WeChatConfig config;
    private final RestTemplate rest = new RestTemplate();

    /**
     * ① 简易测试推送：直接调用 sendAlarmTemplate，跳过关注检查
     * GET /api/wechat/testNotify?userId=10
     */
    @GetMapping("/testNotify")
    public String testNotify(@RequestParam Long userId) {
        // 查 openid
        UserOpenid uo = openidMapper.selectOne(
                new QueryWrapper<UserOpenid>().eq("user_id", userId)
        );
        if (uo == null) {
            return "❌ 用户 " + userId + " 尚未绑定 openid";
        }
        String openid = uo.getOpenid();

        // （可选）检查关注状态
        if (!userService.isSubscribed(openid)) {
            return "❌ openid=" + openid + " 未关注公众号，无法推送消息";
        }

        // 当前时间
        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 调用新版 sendAlarmTemplate，需要填充五个字段和 eventId
        messageService.sendAlarmTemplate(
                openid,
                "测试项目",            // projectName（任填示例）
                "测试设备",            // deviceName
                "温度超限：当前 99℃", // alarmReason（生成原因）
                "DEV-20250529-001",    // deviceId（设备编号示例）
                now,                  // triggerTime（生成时间）
                "紧急",               // alertLevel（状态码）
                123L                  // eventId（用于拼接详情页 URL）
        );

        return "✅ 模板消息已推送给 openid=" + openid;
    }

    /**
     * ② 调试接口：返回 token、userInfo 和 templateSend 原始 JSON
     * GET /api/wechat/debug?userId=10
     */
    @GetMapping("/debug")
    public Map<String, Object> debug(@RequestParam Long userId,
                                     @RequestParam(defaultValue = "false") boolean force) {

        // 1) 如明确想刷新，带 ?force=true；否则直接用缓存
        if (force) {
            tokenService.refreshToken(true);   // 强制刷新
        }
        String token = tokenService.getTokenSafely();   // 统一入口

        // 2) 拿 openid
        UserOpenid uo = openidMapper.selectOne(
                new QueryWrapper<UserOpenid>().eq("user_id", userId));
        String openid = uo == null ? null : uo.getOpenid();

        // 3) 调 userInfo
        String userInfoResp = "N/A";
        if (openid != null) {
            String infoUrl = String.format(
                    "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN",
                    token, openid);
            userInfoResp = rest.getForObject(infoUrl, String.class);
        }

        // 4) 再调模板消息（用刚才 token）
        String sendResp = "N/A";
        if (openid != null) {
            JSONObject payload = buildDebugPayload(openid);   // 抽个方法更简洁
            String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
            sendResp = rest.postForObject(sendUrl, payload.toJSONString(), String.class);
        }

        return Map.of(
                "access_token", token,
                "openid", openid,
                "userInfoApi", userInfoResp,
                "templateSend", sendResp
        );
    }

    /**
     * 把 payload 构造单独封装，避免 debug 方法过长
     */
    private JSONObject buildDebugPayload(String openid) {
        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JSONObject data = new JSONObject()
                .fluentPut("thing8", new JSONObject().fluentPut("value", "调试设备名称"))
                .fluentPut("character_string13", new JSONObject().fluentPut("value", "DEVICE-TEST-001"))
                .fluentPut("time5", new JSONObject().fluentPut("value", now))
                .fluentPut("thing6", new JSONObject().fluentPut("value", "调试原因：温度超限"))
                .fluentPut("character_string12", new JSONObject().fluentPut("value", "重要"));

        return new JSONObject()
                .fluentPut("touser", openid)
                .fluentPut("template_id", config.getAlarmTemplateId())
                .fluentPut("url", "https://iotclouds.top/")
                .fluentPut("data", data);
    }
}

