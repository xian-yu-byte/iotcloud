package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.Common.user.UserDetail;

public interface UserService {

    Long findUserIdByUsername(String username);

}
