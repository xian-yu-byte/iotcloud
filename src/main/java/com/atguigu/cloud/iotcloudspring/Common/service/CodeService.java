package com.atguigu.cloud.iotcloudspring.Common.service;

import org.springframework.stereotype.Service;

@Service
public class CodeService {
    public String nextCode() {
        return "AGT-" + System.currentTimeMillis();
    }
}
