package com.atguigu.cloud.iotcloudspring.Common.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * Copyright (c) 人人开源 All rights reserved.
 * Website: https://www.renren.io
 */
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ObjectMapper objectMapper;
    /**
     * 默认过期时长为24小时，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**
     * 过期时长为1小时，单位：秒
     */
    public final static long HOUR_ONE_EXPIRE = (long) 60 * 60;
    /**
     * 过期时长为6小时，单位：秒
     */
    public final static long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1L;

    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public Object get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
        return value;
    }

    public Object get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<String, Object> hGetAll(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public void hMSet(String key, Map<String, Object> map) {
        hMSet(key, map, DEFAULT_EXPIRE);
    }

    public void hMSet(String key, Map<String, Object> map, long expire) {
        redisTemplate.opsForHash().putAll(key, map);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void hSet(String key, String field, Object value) {
        hSet(key, field, value, DEFAULT_EXPIRE);
    }

    public void hSet(String key, String field, Object value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public void hDel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public void leftPush(String key, Object value) {
        leftPush(key, value, DEFAULT_EXPIRE);
    }

    public void leftPush(String key, Object value, long expire) {
        redisTemplate.opsForList().leftPush(key, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }


    /**
     * 清空所有 Redis 数据库中的所有键
     */
    public void emptyAll() {
        // Lua 脚本 FLUSHALL是redis清空所有库的命令
        String luaScript = "redis.call('FLUSHALL')";

        // 创建 DefaultRedisScript 对象
        DefaultRedisScript<Void> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript); // 设置 Lua 脚本内容
        redisScript.setResultType(Void.class); // 设置返回值类型

        // 执行 Lua 脚本
        List<String> keys = Collections.emptyList(); // 如果脚本不依赖 key，可以传入空列表
        redisTemplate.execute(redisScript, keys);

    }

    /**
     * 通用的带类型转换的 get
     *
     * @param key   Redis key
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 转换后的对象，无法转换时返回 null
     */
    public <T> T get(String key, Class<T> clazz) {
        Object val = redisTemplate.opsForValue().get(key);
        if (val == null) {
            return null;
        }
        // 如果直接已经是目标类型，直接返回
        if (clazz.isInstance(val)) {
            return clazz.cast(val);
        }
        // 否则用 Jackson 做二次转换
        try {
            return objectMapper.convertValue(val, clazz);
        } catch (IllegalArgumentException e) {
            // 转换失败，记录下日志或忽略
            return null;
        }
    }

    /**
     * 专门取 Map<String,Object> 的版本
     */
    public Map<String, Object> getMap(String key) {
        Object val = redisTemplate.opsForValue().get(key);
        if (val == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(
                    val, new TypeReference<Map<String, Object>>() {
                    }
            );
        } catch (IllegalArgumentException e) {
            return Collections.emptyMap();
        }
    }

    public <T> T hGet(String key, String field, Class<T> clazz) {
        Object val = redisTemplate.opsForHash().get(key, field);
        if (val == null) return null;
        // 如果本身就是目标类型，直接返回
        if (clazz.isInstance(val)) {
            return clazz.cast(val);
        }
        // 否则用 Jackson 转一下
        return objectMapper.convertValue(val, clazz);
    }
}