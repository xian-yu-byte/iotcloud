package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.pojo.logs.logs;

import java.util.List;

public interface LogService {
    /**
     * 分页查询日志
     */
    List<logs> listLogs(int page, int size);

    /**
     * 获取日志总数
     */
    long countLogs();

    /**
     * 添加日志
     */
    void addLog(String username, String description);
    
    /**
     * 更新日志
     */
    boolean updateLog(logs log);
    
    /**
     * 删除日志
     */
    boolean deleteLog(Long id);
    
    /**
     * 根据ID查询日志
     */
    logs getLogById(Long id);
}
