package com.atguigu.cloud.iotcloudspring.WeChat.service.Impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeChatUserService {
    private final WeChatTokenService tokenService;
    private final RestTemplate rest = new RestTemplate();

    /**
     * 查询用户是否已关注服务号
     * @param openid 微信 openid
     * @return true=已关注，false=未关注或不存在
     */
    public boolean isSubscribed(String openid) {
        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN",
                tokenService.getAccessToken(),
                openid
        );
        // 调微信接口
        String resp = rest.getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(resp);
        // subscribe=1 表示已关注，0 表示未关注
        return json.getIntValue("subscribe") == 1;
    }
}
