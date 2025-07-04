package com.atguigu.cloud.iotcloudspring.WeChat.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.cloud.iotcloudspring.WeChat.config.WeChatConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
//public class WeChatTokenService {
//    private final WeChatConfig config;
//    private final RestTemplate rest = new RestTemplate();
//
//    @Getter
//    private volatile String accessToken;
//
//    @PostConstruct
//    public void init() {
//        refreshToken();
//    }
//
//    /** 应用启动后立即去微信拉一次 */
//    @Scheduled(initialDelay = 1000, fixedDelay = 7000_000) // 每隔 7000 秒刷新一次
//    public void refreshToken() {
//        String url = String.format(
//                "https://api.weixin.qq.com/cgi-bin/token?" +
//                        "grant_type=client_credential&appid=%s&secret=%s",
//                config.getAppId(), config.getSecret()
//        );
//        String resp = rest.getForObject(url, String.class);
//        JSONObject json = JSONObject.parseObject(resp);
//        this.accessToken = json.getString("access_token");
//        // 后续考虑加上失败重试、日志等
//    }
//}

public class WeChatTokenService {

    private final WeChatConfig config;
    private final RestTemplate restTemplate;
    private final RedissonClient redisson;
    private final StringRedisTemplate redis;

    private static final String TOKEN_KEY  = "wx:access_token";
    private static final String TOKEN_LOCK = "wx:access_token:lock";

    /** 对外统一调用 */
    public String getTokenSafely() {
        String token = redis.opsForValue().get(TOKEN_KEY);
        if (StringUtils.hasText(token)) return token;

        // 双检 + 分布式锁
        RLock lock = redisson.getLock(TOKEN_LOCK);
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                token = redis.opsForValue().get(TOKEN_KEY);
                if (StringUtils.hasText(token)) return token;
                return refreshTokenInner(false);            // 普通模式
            } else {
                // 没拿到锁，等 100 ms 再去缓存拿
                Thread.sleep(100);
                return Objects.requireNonNull(redis.opsForValue().get(TOKEN_KEY),
                        "token 获取超时");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("获取 token 被中断", ie);
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    /**
     * 强制刷新（例如检测到 40001 时调用）
     * force=true 会把旧 token 彻底作废
     */
    public String refreshToken(boolean force) {
        RLock lock = redisson.getLock(TOKEN_LOCK);
        try {
            lock.lock(5, TimeUnit.SECONDS);
            return refreshTokenInner(force);
        } finally {
            lock.unlock();
        }
    }

    // -------------- 私有：真正去微信拿 token ----------------
    private String refreshTokenInner(boolean forceRefresh) {
        JSONObject req = new JSONObject()
                .fluentPut("grant_type", "client_credential")
                .fluentPut("appid",  config.getAppId())
                .fluentPut("secret", config.getSecret())
                .fluentPut("force_refresh", forceRefresh);

        String resp = restTemplate.postForObject(
                "https://api.weixin.qq.com/cgi-bin/stable_token",   // 稳定版接口
                req.toJSONString(), String.class);

        JSONObject json = JSONObject.parseObject(resp);
        if (json.getInteger("errcode") != null && json.getIntValue("errcode") != 0) {
            throw new IllegalStateException("拉取 token 失败：" + resp);
        }
        String token     = json.getString("access_token");
        int    expiresIn = json.getIntValue("expires_in");          // 官方一般 7200s

        // 提前 5 min 过期，保证高并发下安全
        redis.opsForValue().set(TOKEN_KEY, token,
                Duration.ofSeconds(expiresIn - 300));

        return token;
    }

    // -------------- 定时任务：仅作保险 ----------------
    @Scheduled(fixedRate = 6_000_000)   // 100 min
    public void scheduledRefresh() {
        // 如果还有 >5 min 就不刷新
        Long ttl = redis.getExpire(TOKEN_KEY, TimeUnit.SECONDS);
        if (ttl != null && ttl > 300) return;
        refreshToken(false);            // 普通模式
    }
}