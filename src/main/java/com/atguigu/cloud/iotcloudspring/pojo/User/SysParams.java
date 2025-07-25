package com.atguigu.cloud.iotcloudspring.pojo.User;

import com.atguigu.cloud.iotcloudspring.Common.service.impl.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_params")
public class SysParams extends BaseEntity {
    /**
     * 参数编码
     */
    private String paramCode;
    /**
     * 参数值
     */
    private String paramValue;
    /**
     * 值类型：string-字符串，number-数字，boolean-布尔，array-数组
     */
    private String valueType;
    /**
     * 类型 0：系统参数 1：非系统参数
     */
    private Integer paramType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

}
