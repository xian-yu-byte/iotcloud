package com.atguigu.cloud.iotcloudspring.Common.service.impl;

import com.atguigu.cloud.iotcloudspring.Common.service.CommonService;
import com.atguigu.cloud.iotcloudspring.mapper.AiMapper;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgent;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl extends BaseServiceImpl<AiMapper, AiAgent> implements CommonService {
}
