package com.atguigu.cloud.iotcloudspring.pojo.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String username;

    @JsonProperty("oldPassword")  // 前端传来的旧密码字段名
    private String oldPassword;

    @JsonProperty("newPassword")  // 前端传来的新密码字段名
    private String newPassword;
}
