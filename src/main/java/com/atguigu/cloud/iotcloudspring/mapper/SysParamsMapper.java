package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.Common.dao.BaseDao;
import com.atguigu.cloud.iotcloudspring.pojo.User.SysParams;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysParamsMapper extends BaseDao<SysParams> {
    /**
     * 根据参数编码，查询value
     *
     * @param paramCode 参数编码
     * @return 参数值
     */
    String getValueByCode(String paramCode);
}
