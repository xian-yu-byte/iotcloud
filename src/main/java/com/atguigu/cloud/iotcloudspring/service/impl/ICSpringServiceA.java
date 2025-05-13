package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.DTO.User.UserResponse;
import com.atguigu.cloud.iotcloudspring.VO.ProjectMember;
import com.atguigu.cloud.iotcloudspring.mapper.ICMapper;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.User.UserProject;
import com.atguigu.cloud.iotcloudspring.pojo.User.users;
import com.atguigu.cloud.iotcloudspring.service.ICSpringService;
import com.atguigu.cloud.iotcloudspring.service.UserService;
import com.atguigu.cloud.iotcloudspring.until.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ICSpringServiceA implements ICSpringService {

    @Resource
    private ICMapper icMapper;

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;  // 注入 JwtUtil 实例

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 前端获取所有项目
     */

    @Override
    public List<ProjectAdd> Getproject(String username) {
        return icMapper.GetProject(username);
    }

    /**
     * 用户创建新项目
     */

    @Override
    public boolean addProject(ProjectAdd project) {
        return icMapper.AddProject(project) > 0;
    }

    /**
     * 用户进行注册
     */

    @Override
    public Result register(users user) {
        // 检查用户名是否存在
        users existUser = icMapper.selectUserByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名重复");
        }

        // 密码加密
        String raw = user.getPassword();
        String hash = passwordEncoder.encode(raw);
        user.setPassword(hash);

        // （可选）secret_key 保持默认或按业务赋值
        // user.setSecret_key(null);

        // 插入数据库
        Long rows = icMapper.RegisteredUser(user);
        if (rows > 0) {
            return Result.success("注册成功");
        } else {
            return Result.error("注册失败");
        }
    }

    /**
     * 用户登录
     */

    @Override
    public Result login(String username, String password) {
        // 查找用户名是否存在
        users existUser = icMapper.selectUserByUsername(username);
        if (existUser == null) {
            return Result.error("用户名不存在");
        }

        // 校验密码是否正确
        if (!passwordEncoder.matches(password, existUser.getPassword())) {
            return Result.error("密码错误");
        }

        // 使用统一密钥生成 token
        String token = jwtUtil.generateToken(username);
        UserResponse userResponse = new UserResponse(token, existUser.getId());

        // 返回登录成功和 token
        return Result.success(userResponse);
    }

    /**
     * 用户改变密码(根据传入的用户名和新密码来进行判断)
     */

    @Override
    public boolean changePassword(String username, String newPassword) {
        // 先查询用户是否存在
        users existUser = icMapper.selectUserByUsername(username);
        if (existUser == null) {
            return false; // 用户不存在
        }

        // 修改密码并返回更新条数是否大于 0
        Long rows = icMapper.updatePassword(username, newPassword);
        return rows > 0;
    }

    @Override
    public Result<Void> joinProject(Long userid, Long projectid, String role) {
        // 先查询该用户是否已加入该项目
        UserProject existingRecord = icMapper.selectUserProjectByUserIdAndProjectId(userid, projectid);
        if (existingRecord != null) {
            return Result.error("你已经加入该项目");
        }
        // 如果不存在，则执行插入操作
        UserProject userProject = new UserProject();
        userProject.setUserid(userid);
        userProject.setProjectid(projectid);
        userProject.setRole(role);
        userProject.setJoinedat(LocalDateTime.now());
        Long rows = icMapper.insertUserProject(userProject);
        return rows > 0 ? Result.success() : Result.error("加入项目失败");
    }

    @Override
    public Result<List<UserProject>> getProjectsByUser(Long userid) {
        List<UserProject> list = icMapper.selectByUserId(userid);
        return Result.success(list);
    }

    @Override
    public Result<List<ProjectAdd>> getProjectsByUsername(String username) {
        // 这里假设 projectadd 表中 admin 字段存放创建者的用户名
        // 先通过 UserService 检查该用户名是否存在
        Long userid = userService.findUserIdByUsername(username);
        if (userid == null) {
            return Result.error("该用户名不存在");
        }
        List<ProjectAdd> projects = icMapper.selectProjectsByAdmin(username);
        return Result.success(projects);
    }

    @Override
    public Result<Void> leaveProject(Long userid, Long projectid) {
        Long rows = icMapper.deleteUserProject(userid, projectid);
        return rows > 0 ? Result.success() : Result.error("退出项目失败");
    }

    @Override
    public Result<Void> joinProjectByName(Long userid, String projectname) {
        // 根据项目名称查询项目
        ProjectAdd project = icMapper.selectProjectByName(projectname);
        if (project == null) {
            return Result.error("项目不存在");
        }
        Long projectid = project.getId();
        // 调用 joinProject 方法插入关联记录
        return joinProject(userid, projectid, "member");
    }

    @Override
    public Result<List<ProjectAdd>> getJoinedProjects(Long userid) {
        List<ProjectAdd> projects = icMapper.selectJoinedProjectsByUser(userid);
        return Result.success(projects);
    }

    @Override
    public Result<ProjectAdd> getProjectById(Long projectid) {
        ProjectAdd project = icMapper.selectProjectById(projectid);
        return project != null ? Result.success(project) : Result.error("项目不存在");
    }

    //  邀请用户加入项目
    @Override
    public Result<Void> inviteProject(Long projectid, String username, String role) {
        // 获取当前登录用户的用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 判断是否是在邀请自己
        if (currentUsername.equals(username)) {
            return Result.error("不能邀请自己加入项目");
        }
        // 查找用户名是否存在
        users existUser = icMapper.selectUserByUsername(username);
        if (existUser == null) {
            return Result.error("用户名不存在");
        }
        //  查询用户是否已加入该项目
        UserProject existingRecord = icMapper.selectUserProjectByUserIdAndProjectId(existUser.getId(), projectid);
        if (existingRecord != null) {
            return Result.error("用户已加入该项目");
        }

        return icMapper.addUserProject(existUser.getId(), projectid, role) ? Result.success() : Result.error("邀请用户加入项目失败");
    }

    //获取当前项目所有的用户列表
    @Override
    public List<ProjectMember> getProjectMember(Integer projectId) {
        return icMapper.getProjectMember(projectId);
    }

    //修改用户角色
    @Override
    public Boolean changeRole(Long userid, String role) {
        return icMapper.changeRole(userid, role) > 0;
    }

    //移除项目成员
    @Override
    public Boolean removeMember(Long userid) {
        return icMapper.removeMember(userid) > 0;
    }
}

