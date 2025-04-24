package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.Common.dao.BaseDao;
import com.atguigu.cloud.iotcloudspring.pojo.Model.AiModelConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper
public interface ModelMapper extends BaseDao<AiModelConfig> {
    List<String> getModelCodeList(@Param("modelType") String modelType, @Param("modelName") String modelName);
}
