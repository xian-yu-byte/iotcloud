package com.atguigu.cloud.iotcloudspring.Rule.mapper;

import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleAction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleActionMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RuleAction action);

    List<RuleAction> selectByRuleId(@Param("ruleId") Long ruleId);
}
