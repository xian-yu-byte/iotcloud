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
        return echostr;
    }

    // 接收微信服务器推送消息（POST）
    @PostMapping
    public String receiveMessage(@RequestBody String xmlData) {
        System.out.println("接收到微信推送消息: " + xmlData);
        // 后续可以解析消息并做业务逻辑处理
        return "success";
    }

    @GetMapping("/api/wechat/bind")
    public void bindOpenid(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {
        Long userId = Long.valueOf(state);

        weChatService.bindOpenid(userId, code);

        // 重定向的网页链接，暂时没写，后续考虑写不写吧
        response.sendRedirect("https://iotclouds.top/bind-success.html");
    }
}
