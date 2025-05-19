package com.atguigu.cloud.iotcloudspring.Rule.mapper;

import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleTargetsDTO;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleTarget;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleTargetMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RuleTarget target);

    /**
     * 查询某 project 下所有规则及其绑定目标，并带上目标名称
     */
    List<RuleTargetsDTO> selectByProjectId(@Param("projectId") Long projectId,@Param("ruleId") Long ruleId);
}
