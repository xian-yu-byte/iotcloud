package com.atguigu.cloud.iotcloudspring.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.atguigu.cloud.iotcloudspring.Common.constant.Constant;
import com.atguigu.cloud.iotcloudspring.Common.page.PageData;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisKeys;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisUtils;
import com.atguigu.cloud.iotcloudspring.Common.service.impl.BaseServiceImpl;
import com.atguigu.cloud.iotcloudspring.Common.utils.ConvertUtils;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.TimbrePageDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.VoiceDTO;
import com.atguigu.cloud.iotcloudspring.VO.TimbreDetailsVO;
import com.atguigu.cloud.iotcloudspring.mapper.TimbreMapper;
import com.atguigu.cloud.iotcloudspring.pojo.Timbre.AiTtsVoice;
import com.atguigu.cloud.iotcloudspring.service.TimbreService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimbreServiceImpl extends BaseServiceImpl<TimbreMapper, AiTtsVoice> implements TimbreService {
    @Autowired
    @Qualifier("timbreMapper")
    private TimbreMapper timbreMapper;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public String getTimbreNameById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }

        String cachedName = redisUtils.get(RedisKeys.getTimbreNameById(id), String.class);

        if (StringUtils.isNotBlank(cachedName)) {
            return cachedName;
        }

        AiTtsVoice entity = timbreMapper.selectById(id);
        if (entity != null) {
            String name = entity.getName();
            if (StringUtils.isNotBlank(name)) {
                redisUtils.set(RedisKeys.getTimbreNameById(id), name);
            }
            return name;
        }

        return null;
    }

    @Override
    public PageData<TimbreDetailsVO> page(TimbrePageDTO dto) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constant.PAGE, dto.getPage());
        params.put(Constant.LIMIT, dto.getLimit());
        IPage<AiTtsVoice> page = timbreMapper.selectPage(
                getPage(params, null, true),
                // 定义查询条件
                new QueryWrapper<AiTtsVoice>()
                        // 必须按照ttsID查找
                        .eq("tts_model_id", dto.getTtsModelId())
                        // 如果有音色名字，按照音色名模糊查找
                        .like(StringUtils.isNotBlank(dto.getName()), "name", dto.getName()));

        return getPageData(page, TimbreDetailsVO.class);
    }

    @Override
    public List<VoiceDTO> getVoiceNames(String ttsModelId, String voiceName) {
        QueryWrapper<AiTtsVoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tts_model_id", StringUtils.isBlank(ttsModelId) ? "" : ttsModelId);
        if (StringUtils.isNotBlank(voiceName)) {
            queryWrapper.like("name", voiceName);
        }
        List<AiTtsVoice> timbreEntities = timbreMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(timbreEntities)) {
            return null;
        }

        return ConvertUtils.sourceToTarget(timbreEntities, VoiceDTO.class);
    }

    @Override
    public TimbreDetailsVO get(String timbreId) {
        if (StringUtils.isBlank(timbreId)) {
            return null;
        }

        // 先从Redis获取缓存
        String key = RedisKeys.getTimbreDetailsKey(timbreId);
        TimbreDetailsVO cachedDetails = (TimbreDetailsVO) redisUtils.get(key);
        if (cachedDetails != null) {
            return cachedDetails;
        }

        // 如果缓存中没有，则从数据库获取
        AiTtsVoice entity = timbreMapper.selectById(timbreId);
        if (entity == null) {
            return null;
        }

        // 转换为VO对象
        TimbreDetailsVO details = ConvertUtils.sourceToTarget(entity, TimbreDetailsVO.class);

        // 存入Redis缓存
        if (details != null) {
            redisUtils.set(key, details);
        }

        return details;
    }

}
