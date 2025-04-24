package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.Common.redis.RedisKeys;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisUtils;
import com.atguigu.cloud.iotcloudspring.Common.service.impl.BaseServiceImpl;
import com.atguigu.cloud.iotcloudspring.Common.utils.ConvertUtils;
import com.atguigu.cloud.iotcloudspring.mapper.ModelMapper;
import com.atguigu.cloud.iotcloudspring.pojo.Model.AiModelConfig;
import com.atguigu.cloud.iotcloudspring.service.ModelConfigService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModelConfigServiceImpl extends BaseServiceImpl<ModelMapper, AiModelConfig>
        implements ModelConfigService {
//    @Autowired
    @Autowired
    @Qualifier("modelMapper")
    private ModelMapper modelMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public AiModelConfig getModelById(String id, boolean isCache) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        if (isCache) {
            AiModelConfig cachedConfig = redisUtils.get(
                    RedisKeys.getModelConfigById(id),
                    AiModelConfig.class);
            if (cachedConfig != null) {
                return ConvertUtils.sourceToTarget(cachedConfig, AiModelConfig.class);
            }
        }
        AiModelConfig entity = modelMapper.selectById(id);
        if (entity != null) {
            redisUtils.set(RedisKeys.getModelConfigById(id), entity);
        }
        return entity;
    }

}
