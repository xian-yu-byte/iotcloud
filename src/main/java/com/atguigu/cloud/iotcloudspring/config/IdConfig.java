package com.atguigu.cloud.iotcloudspring.config;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdConfig {
    @Bean
    public Snowflake snowflake() {
        // datacenterId = 1, workerId = 1
        return IdUtil.getSnowflake(1, 1);
    }
}
