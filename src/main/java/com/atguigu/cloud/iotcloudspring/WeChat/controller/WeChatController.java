package com.atguigu.cloud.iotcloudspring.WeChat.controller;

import com.atguigu.cloud.iotcloudspring.WeChat.service.WeChatService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/IC/wx")
@CrossOrigin("*")
public class WeChatController {

    @Resource
    private WeChatService weChatService;

    // 微信服务器首次验证接口
    @GetMapping
    public String validateWX(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr) {

        // 你可以在这里添加签名校验（推荐），但如果只是调通可以先返回 echostr
        return echostr;
    }

    // 接收微信服务器推送消息（POST）
    @PostMapping
    public String receiveMessage(@RequestBody String xmlData) {
        // 你可以先打印出来看看收到的 XML
        System.out.println("接收到微信推送消息: " + xmlData);

        // 后续可以解析消息并做业务逻辑处理

        // 返回成功（或加密后的 success）
        return "success";
    }

    @GetMapping("/api/wechat/bind")
    public void bindOpenid(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {
        // 1. 从 state 中取出平台用户 ID
        Long userId = Long.valueOf(state);

        // 2. 调用 Service 换取 openid 并保存到 user_openid 表
        weChatService.bindOpenid(userId, code);

        // 3. 绑定成功后，重定向到前端的“绑定成功”提示页面
        response.sendRedirect("https://iotclouds.top/bind-success.html");
    }
}
