package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.mapper.LogMapper;
import com.atguigu.cloud.iotcloudspring.pojo.logs.logs;
import com.atguigu.cloud.iotcloudspring.service.LogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public List<logs> listLogs(int page, int size) {
        int offset = (page - 1) * size;
        return logMapper.selectLogs(offset, size);
    }

    @Override
    public long countLogs() {
        return logMapper.countLogs();
    }

    @Override
    public void addLog(Long userId, String description) {
        logs log = new logs();
        log.setUserId(userId);
        log.setDescription(description);
        logMapper.insertLog(log);
    }
}
