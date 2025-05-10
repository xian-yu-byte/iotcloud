package com.atguigu.cloud.iotcloudspring.config;

import com.atguigu.cloud.iotcloudspring.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 构造函数注入
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 1) 开启 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2) 禁用 CSRF
                .csrf(csrf -> csrf.disable())
                // 3) 配置访问权限
                .authorizeHttpRequests(auth -> auth
                        // 先把所有预检 OPTIONS 放行
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 真正需要认证的接口
                        .requestMatchers("/IC/GetProject").authenticated()
                        // 其它接口都放行
                        .anyRequest().permitAll()
                )
                // 4) 在 JWT 过滤器之前加上你的自定义过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 放行所有来源
        config.setAllowedOriginPatterns(List.of("*"));
        // 放行所有方法
        config.setAllowedMethods(List.of("*"));
        // 放行所有请求头
        config.setAllowedHeaders(List.of("*"));
        // 允许携带 Cookie
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径生效
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
