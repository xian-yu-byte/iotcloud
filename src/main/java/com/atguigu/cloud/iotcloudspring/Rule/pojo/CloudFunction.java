package com.atguigu.cloud.iotcloudspring.Rule.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("cloud_function")
@Data
public class CloudFunction {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String script;
    private String runtime;
}
