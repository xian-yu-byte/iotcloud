package com.atguigu.cloud.iotcloudspring.pojo.User;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordRequest {
    private String username;

    @JsonProperty("oldPassword")  // 前端传来的旧密码字段名
    private String oldPassword;

    @JsonProperty("newPassword")  // 前端传来的新密码字段名
    private String newPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
