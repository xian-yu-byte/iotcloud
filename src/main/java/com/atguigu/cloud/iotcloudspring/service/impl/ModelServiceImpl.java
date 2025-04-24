package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.Common.redis.RedisKeys;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisUtils;
import com.atguigu.cloud.iotcloudspring.Common.service.impl.BaseServiceImpl;
import com.atguigu.cloud.iotcloudspring.mapper.ModelMapper;
import com.atguigu.cloud.iotcloudspring.pojo.Model.AiModelConfig;
import com.atguigu.cloud.iotcloudspring.service.ModelService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl extends BaseServiceImpl<ModelMapper, AiModelConfig> implements ModelService {

    @Autowired
    @Qualifier("modelMapper")
    private ModelMapper modelMapper;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public String getModelNameById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }

        String cachedName = (String) redisUtils.get(RedisKeys.getModelNameById(id));

        if (StringUtils.isNotBlank(cachedName)) {
            return cachedName;
        }

        AiModelConfig entity = modelMapper.selectById(id);
        if (entity != null) {
            String modelName = entity.getModelName();
            if (StringUtils.isNotBlank(modelName)) {
                redisUtils.set(RedisKeys.getModelNameById(id), modelName);
            }
            return modelName;
        }

        return null;
    }
}
