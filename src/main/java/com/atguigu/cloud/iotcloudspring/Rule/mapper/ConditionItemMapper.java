package com.atguigu.cloud.iotcloudspring.Rule.mapper;

import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleConditionItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConditionItemMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")

    int insert(RuleConditionItem item);

    List<RuleConditionItem> selectByGroupId(@Param("groupId") Long groupId);

}
