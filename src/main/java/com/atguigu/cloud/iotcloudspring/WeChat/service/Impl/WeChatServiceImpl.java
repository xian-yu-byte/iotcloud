package com.atguigu.cloud.iotcloudspring.WeChat.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.cloud.iotcloudspring.WeChat.config.WeChatConfig;
import com.atguigu.cloud.iotcloudspring.WeChat.mapper.UserOpenidMapper;
import com.atguigu.cloud.iotcloudspring.WeChat.pojo.UserOpenid;
import com.atguigu.cloud.iotcloudspring.WeChat.service.WeChatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeChatServiceImpl implements WeChatService {
    private final WeChatConfig config;
    private final UserOpenidMapper openidMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void bindOpenid(Long userId, String code) {
        // 1. 向微信换取 openid
        String url = String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token" +
                        "?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                config.getAppId(), config.getSecret(), code
        );
        String resp = restTemplate.getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(resp);
        String openid = json.getString("openid");
        if (openid == null) {
            throw new RuntimeException("Failed to get openid from WeChat: " + resp);
        }

        // 2. 查询是否已有绑定记录
        QueryWrapper<UserOpenid> qw = new QueryWrapper<UserOpenid>().eq("user_id", userId);
        UserOpenid existing = openidMapper.selectOne(qw);

        // 3. 插入或更新
        if (existing == null) {
            UserOpenid entity = new UserOpenid();
            entity.setUserId(userId);
            entity.setOpenid(openid);
            openidMapper.insert(entity);
        } else {
            existing.setOpenid(openid);
            openidMapper.updateById(existing);
        }
    }
}
