package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.DTO.TrainingLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrainingLogMapper {
    /**
     * 查询所有有训练日志的 project_id
     */
    List<Long> selectAllProjectIds();

    /**
     * 根据 projectId 获取日志
     */
    TrainingLog selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 新增日志（第一次训练）
     */
    int insert(TrainingLog log);

    /**
     * 更新日志
     */
    int update(TrainingLog log);
}
