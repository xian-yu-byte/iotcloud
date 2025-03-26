package com.atguigu.cloud.iotcloudspring.until;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // 从配置文件中读取统一密钥，例如在 application.properties 中配置：jwt.secret=yourSecretKey
    @Value("${spring.jwt.secret}")
    private String secretKey;

    /**
     * 生成 JWT Token
     * @param username 用户名
     * @return token 字符串
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                // 可选：设置 token 的过期时间，例如一天
                // .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 解析 JWT Token，获取用户名
     * @param token JWT token
     * @return token 中存储的用户名，如果解析失败返回 null
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // 可以在这里记录异常信息
            return null;
        }
    }
}
