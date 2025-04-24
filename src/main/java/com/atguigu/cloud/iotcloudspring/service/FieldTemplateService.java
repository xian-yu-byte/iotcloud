package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.pojo.device.FieldTemplate;

import java.util.List;

public interface FieldTemplateService {
    // 查询模板表的数据
    List<FieldTemplate> listAll();
}
