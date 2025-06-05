package com.atguigu.cloud.iotcloudspring.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate rt = builder.build();

        rt.getMessageConverters().removeIf(c -> c instanceof StringHttpMessageConverter);

        rt.getMessageConverters().addFirst(
                new StringHttpMessageConverter(StandardCharsets.UTF_8));

        return rt;
    }
}
