package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.mapper.FieldTemplateMapper;
import com.atguigu.cloud.iotcloudspring.pojo.device.FieldTemplate;
import com.atguigu.cloud.iotcloudspring.service.FieldTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FieldTemplateServiceImpl implements FieldTemplateService {
    private final FieldTemplateMapper fieldTemplateMapper;

    @Override
    public List<FieldTemplate> listAll() {
        return fieldTemplateMapper.selectAll();
    }
}
