package com.atguigu.cloud.iotcloudspring.WeChat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOpenid {
    private Long id;
    private Long userId;
    private String openid;
    private String createTime;
    private String updateTime;
}
