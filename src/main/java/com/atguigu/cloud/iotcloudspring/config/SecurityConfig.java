package com.atguigu.cloud.iotcloudspring.config;

import com.atguigu.cloud.iotcloudspring.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
        // 这里通过 Lambda 风格配置 Security
        return http
                // 1) 开启 CORS，并使用下面的 corsConfigurationSource()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2) 禁用 CSRF
                .csrf(csrf -> csrf.disable())
                // 3) 配置访问权限
                .authorizeHttpRequests(auth -> auth
                        //IC/GetProject 需要认证，其他都放行
                        .requestMatchers("/IC/GetProject").authenticated()
                        .anyRequest().permitAll()
                )
                // 4) 把自定义的 JWT 过滤器加到 Spring Security 过滤器链之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 配置 CORS 策略，允许前端的跨域请求
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许的前端地址（开发环境可用 *，生产环境建议写具体域名）
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://iotcloud.yundatech.top","https://d318-125-220-160-45.ngrok-free.app"));
        // 允许的请求方法
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        // 允许的请求头
        config.setAllowedHeaders(List.of("*"));
        // 是否允许携带 Cookie
        config.setAllowCredentials(true);

        // 映射到所有接口
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
