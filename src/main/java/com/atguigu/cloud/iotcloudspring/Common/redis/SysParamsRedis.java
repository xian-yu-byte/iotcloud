package com.atguigu.cloud.iotcloudspring.Common.redis;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SysParamsRedis {
    private final RedisUtils redisUtils;

    public void delete(Object[] paramCodes) {
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hDel(key, paramCodes);
    }

    public void set(String paramCode, String paramValue) {
        if (paramValue == null) {
            return;
        }
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hSet(key, paramCode, paramValue);
    }

    public String get(String paramCode) {
        String key = RedisKeys.getSysParamsKey();
        return redisUtils.hGet(key, paramCode, String.class);
    }

}
