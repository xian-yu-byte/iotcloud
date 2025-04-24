package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.Common.redis.SysParamsRedis;
import com.atguigu.cloud.iotcloudspring.Common.service.impl.BaseServiceImpl;
import com.atguigu.cloud.iotcloudspring.Common.utils.ConvertUtils;
import com.atguigu.cloud.iotcloudspring.DTO.User.SysParamsDTO;
import com.atguigu.cloud.iotcloudspring.mapper.AiMapper;
import com.atguigu.cloud.iotcloudspring.mapper.SysParamsMapper;
import com.atguigu.cloud.iotcloudspring.pojo.User.SysParams;
import com.atguigu.cloud.iotcloudspring.service.SysParamsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SysParamsServiceImpl extends BaseServiceImpl<SysParamsMapper, SysParams> implements SysParamsService {
    private final SysParamsRedis sysParamsRedis;
    @Autowired
    @Qualifier("sysParamsMapper")
    private SysParamsMapper sysParamsMapper;

    private QueryWrapper<SysParams> getWrapper(Map<String, Object> params) {
        String paramCode = (String) params.get("paramCode");

        QueryWrapper<SysParams> wrapper = new QueryWrapper<>();
        wrapper.eq("param_type", 1);
        wrapper.like(StringUtils.isNotBlank(paramCode), "param_code", paramCode);

        return wrapper;
    }

    @Override
    public List<SysParamsDTO> list(Map<String, Object> params) {
        List<SysParams> entityList = sysParamsMapper.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysParamsDTO.class);
    }
    @Override
    public String getValue(String paramCode, Boolean fromCache) {
        String paramValue = null;
        if (fromCache) {
            paramValue = sysParamsRedis.get(paramCode);
            if (paramValue == null) {
                paramValue = sysParamsMapper.getValueByCode(paramCode);

                sysParamsRedis.set(paramCode, paramValue);
            }
        } else {
            paramValue = sysParamsMapper.getValueByCode(paramCode);
        }
        return paramValue;
    }
}
