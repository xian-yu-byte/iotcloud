package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.pojo.Timbre.AiTtsVoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper
public interface TimbreMapper extends BaseMapper<AiTtsVoice> {
}
