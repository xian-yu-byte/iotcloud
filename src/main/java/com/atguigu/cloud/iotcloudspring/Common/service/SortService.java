package com.atguigu.cloud.iotcloudspring.Common.service;

import com.atguigu.cloud.iotcloudspring.mapper.AiMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SortService {
    @Resource
    private AiMapper aiMapper; // 或专门的 SortMapper
    public Integer nextSort() {
        Integer maxSort = aiMapper.selectMaxSort();
        return (maxSort == null ? 0 : maxSort) + 1;
    }
}
