package com.atguigu.cloud.iotcloudspring.WeChat.service.Impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeChatUserService {

    private final WeChatTokenService tokenService;
    private final RestTemplate restTemplate;

    /**
     * 查询用户是否已关注服务号
     * @param openid 微信 openid
     * @return true=已关注，false=未关注/查无此人
     */
    public boolean isSubscribed(String openid) {

        // ① 第一次用缓存 token 调用
        String token = tokenService.getTokenSafely();
        Boolean sub = querySubscribe(token, openid);

        // ② token 已失效 → 强制刷新后再查一次
        if (sub == null) {                         // null 表示拿到 40001
            log.warn("access_token 失效，刷新后重试查询用户关注状态");
            token = tokenService.refreshToken(true);   // 强制刷新
            sub = querySubscribe(token, openid);
        }
        // ③ 仍为空就返回 false；否则返回布尔值
        return sub != null && sub;
    }

    /**
     * @return true/false 已关注/未关注；null 代表 token 失效
     */
    private Boolean querySubscribe(String token, String openid) {
        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN",
                token, openid);

        try {
            String resp = restTemplate.getForObject(url, String.class);
            JSONObject json = JSONObject.parseObject(resp);

            Integer errcode = json.getInteger("errcode");
            if (errcode != null && errcode != 0) {
                // 只有 token 失效相关才返回 null，其它错误视为未关注
                if (Set.of(40001, 40014, 42001).contains(errcode)) {
                    return null;
                }
                log.error("微信 user/info 接口错误：{}", resp);
                return false;
            }
            return json.getIntValue("subscribe") == 1;

        } catch (Exception e) {
            log.error("调用 wx user/info 发生异常", e);
            return false;
        }
    }
}
