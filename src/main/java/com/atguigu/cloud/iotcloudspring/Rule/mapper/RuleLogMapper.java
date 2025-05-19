package com.atguigu.cloud.iotcloudspring.Rule.mapper;

import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RuleLogMapper {
    void insert(RuleLog log);
}
