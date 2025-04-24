package com.atguigu.cloud.iotcloudspring.config;

import com.atguigu.cloud.iotcloudspring.Common.user.UserDetail;
import com.atguigu.cloud.iotcloudspring.service.UserService;
import com.atguigu.cloud.iotcloudspring.until.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SecurityUser {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     */
    public Long getUser() {
        String token = getToken();
        if (token == null) {
            return null; // 如果没有 token，返回空用户信息
        }

        String username = jwtUtil.getUsernameFromToken(token);
        if (username == null) {
            return null; // 如果无法从 token 中解析出用户名，返回空用户信息
        }

        // 根据用户名从数据库或其他存储中获取用户详细信息（例如 userId）
        Long userId = userService.findUserIdByUsername(username);
        if (userId == null) {
            return null; // 如果找不到对应的用户信息，返回空用户信息
        }

        return userId;
    }

    /**
     * 获取当前用户的用户名
     */
    public String getUsername() {
        String token = getToken();
        if (token == null) {
            return null;
        }
        return jwtUtil.getUsernameFromToken(token);
    }


    /**
     * 获取当前用户的 Token
     */
    private String getToken() {
        // 这里可以根据你的具体逻辑获取当前请求中的 Token
        // 假设前端将 Token 存在请求头中的 Authorization 字段
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        // 否则直接返回
        return token;
    }

    /**
     * 获取当前用户的 ID
     */
//    public Long getUserId() {
//        UserDetail user = getUser();
//        return user.getId(); // 假设 UserDetail 对象中包含了用户的 ID 字段
//    }
}
