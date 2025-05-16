package com.atguigu.cloud.iotcloudspring.Rule.mapper;

import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleCardDTO;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Rule rule);

    List<RuleCardDTO> getRuleCard(@Param("projectId") Long projectId);

    Boolean updateEnableRule(@Param("ruleId") Long ruleId, @Param("enabled") Boolean enabled);

    List<Rule> selectEnabledByDevice(@Param("deviceId") Long deviceId);
}
