package com.atguigu.cloud.iotcloudspring.Rule.mapper;

import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleConditionGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConditionGroupMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RuleConditionGroup group);

    List<RuleConditionGroup> selectByRuleId(@Param("ruleId") Long ruleId);
}
