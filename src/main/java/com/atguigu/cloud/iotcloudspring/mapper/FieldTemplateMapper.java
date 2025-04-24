package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.pojo.device.FieldTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FieldTemplateMapper {
    List<FieldTemplate> selectAll();
}
