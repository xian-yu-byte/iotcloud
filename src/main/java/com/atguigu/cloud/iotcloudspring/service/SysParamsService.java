package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.Common.service.BaseService;
import com.atguigu.cloud.iotcloudspring.DTO.User.SysParamsDTO;
import com.atguigu.cloud.iotcloudspring.pojo.User.SysParams;

import java.util.List;
import java.util.Map;

public interface SysParamsService extends BaseService<SysParams> {
    List<SysParamsDTO> list(Map<String, Object> params);
    /**
     * 根据参数编码，获取参数的value值
     *
     * @param paramCode 参数编码
     * @param fromCache 是否从缓存中获取
     */
    String getValue(String paramCode, Boolean fromCache);
}
