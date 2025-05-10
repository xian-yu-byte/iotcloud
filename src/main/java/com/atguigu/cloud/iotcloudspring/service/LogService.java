package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.pojo.logs.logs;

import java.util.List;

public interface LogService {
    List<logs> listLogs(int page, int size);

    long countLogs();

    void addLog(Long userId, String description);
}
