package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.mapper.ICMapper;
import com.atguigu.cloud.iotcloudspring.pojo.User.users;
import com.atguigu.cloud.iotcloudspring.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private ICMapper icMapper;

    @Override
    public Long findUserIdByUsername(String username) {
        users user = icMapper.selectUserByUsername(username);
        return user != null ? user.getId() : null;
    }
}