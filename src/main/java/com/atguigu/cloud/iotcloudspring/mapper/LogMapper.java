package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.pojo.logs.logs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {
    /**
     * 按时间倒序分页查询
     */
    List<logs> selectLogs(
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /**
     * 查询总记录数，用于前端分页
     */
    long countLogs();

    /**
     * 插入一条日志，返回影响行数，insert 后会回填 id
     */
    int insertLog(logs log);
    
    /**
     * 更新日志信息
     */
    int updateLog(logs log);
    
    /**
     * 删除日志
     */
    int deleteLog(@Param("id") Long id);
    
    /**
     * 根据ID查询日志
     */
    logs selectLogById(@Param("id") Long id);
}
