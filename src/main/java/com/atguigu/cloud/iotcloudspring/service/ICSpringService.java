package com.atguigu.cloud.iotcloudspring.service;

import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.User.UserProject;
import com.atguigu.cloud.iotcloudspring.pojo.User.users;

import java.util.List;

public interface ICSpringService {
    //根据用户名获取项目
    List<ProjectAdd> Getproject(String username);

    //添加项目
    boolean addProject(ProjectAdd project);

    //删除项目
    Result register(users user);

    //用户登录
    Result login(String username, String password);

    //用户注册
    boolean changePassword(String username, String newPassword);

    // 当前登录用户加入项目（通过项目ID加入）
    Result<Void> joinProject(Long userid, Long projectid, String role);

    // 获取当前用户加入的项目关联记录
    Result<List<UserProject>> getProjectsByUser(Long userid);

    // 根据传入的用户名查询该用户创建的所有项目
    Result<List<ProjectAdd>> getProjectsByUsername(String username);

    // 用户退出项目
    Result<Void> leaveProject(Long userid, Long projectid);

    // 当前登录用户通过项目名称加入项目
    Result<Void> joinProjectByName(Long userid, String projectname);

    // 根据用户ID查询项目信息
    Result<List<ProjectAdd>> getJoinedProjects(Long userid);

    // 根据项目ID查询项目信息
    Result<ProjectAdd> getProjectById(Long projectid);
}
