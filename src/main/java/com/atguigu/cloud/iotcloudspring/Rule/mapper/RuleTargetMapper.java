package com.atguigu.cloud.iotcloudspring.Rule.mapper;

import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleTarget;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface RuleTargetMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RuleTarget target);
}
