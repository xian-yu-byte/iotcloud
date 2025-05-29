package com.atguigu.cloud.iotcloudspring.WeChat.service;

public interface WeChatService {
    /**
     * 绑定指定平台用户与微信 openid
     * @param userId 平台用户 ID
     * @param code   微信 OAuth2 返回的临时授权码
     */
    void bindOpenid(Long userId, String code);
}
