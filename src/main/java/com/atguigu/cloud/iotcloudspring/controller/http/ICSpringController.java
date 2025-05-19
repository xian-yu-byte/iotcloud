package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.DTO.JoinProjectByNameRequest;
import com.atguigu.cloud.iotcloudspring.DTO.JoinProjectRequest;
import com.atguigu.cloud.iotcloudspring.DTO.LeaveProjectRequest;
import com.atguigu.cloud.iotcloudspring.VO.ProjectMember;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectInvitation;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.User.ChangePasswordRequest;
import com.atguigu.cloud.iotcloudspring.pojo.User.UserProject;
import com.atguigu.cloud.iotcloudspring.pojo.User.users;
import com.atguigu.cloud.iotcloudspring.service.ICSpringService;
import com.atguigu.cloud.iotcloudspring.service.UserService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class ICSpringController {
    @Resource
    private ICSpringService iCSpringService;

    @Resource
    private UserService userService;

    // 获取项目
    @GetMapping("GetProject")
    public Result<List<ProjectAdd>> getProject(Authentication authentication) {
        // 1. 从认证信息中拿到当前登录用户
        String currentUsername = authentication.getName();
        // 2. 调用带参方法，只查当前用户的项目
        List<ProjectAdd> projects = iCSpringService.Getproject(currentUsername);
        // 3. 返回结果
        return Result.success(projects);
    }

    // 创建项目
    @PostMapping("/AddProject")
    public Result<String> addProject(
            @RequestBody ProjectAdd project) {
        boolean success = iCSpringService.addProject(project);
        return success ? Result.success("项目创建成功") : Result.error("项目创建失败");
    }

    // 注册
    @PostMapping("/register")
    public Result register(@RequestBody users user) {
        return iCSpringService.register(user);
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody users user) {
        return iCSpringService.login(user.getUsername(), user.getPassword());
    }

    // 修改密码
    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody ChangePasswordRequest request) {
        boolean success = iCSpringService.changePassword(request.getUsername(), request.getNewPassword());
        return success ? Result.success("密码修改成功") : Result.error("用户名不存在或修改失败");
    }

    //用户加入项目
    @PostMapping("/join")
    public Result<Void> joinProject(@RequestBody JoinProjectRequest request,
                                    Authentication authentication) {
        // 从 Authentication 中取出存储的用户名
        String username = (String) authentication.getPrincipal();
        // 通过 UserService 查询该用户名对应的数字 ID
        Long userid = userService.findUserIdByUsername(username);
        if (userid == null) {
            return Result.error("无效的用户标识");
        }
        // 使用查询到的 userid 调用 Service 层加入项目（默认角色 "member"）
        return iCSpringService.joinProject(userid, request.getProjectid(), "member");
    }

    //邀请加入项目
    @PostMapping("/invite")
    public Result<Void> inviteProject(@RequestParam Long projectid,
                                      @RequestParam String username,
                                      @RequestParam String role,
                                      @RequestParam int isInvite) {
        return iCSpringService.inviteProject(projectid, username, role, isInvite);
    }

    //查看发出或收到的邀请
    @GetMapping("/inviteList")
    public Result<List<ProjectInvitation>> getInviteList(String username, Long projectid, Boolean isInviter){
        List<ProjectInvitation> inviteList = iCSpringService.getInviteList(username,projectid,isInviter);
        return Result.success(inviteList);
    }

    //取消或拒绝邀请
    @DeleteMapping("/cancelInvite")
    public Result<Void> cancelInvite(@RequestParam Long inviteId) {
        Boolean success = iCSpringService.cancelInvite(inviteId);
        return success ? Result.success() : Result.error("取消失败");
    }

    //修改用户角色
    @PostMapping("/changeRole")
    public Result<Void> changeRole(@RequestParam Long userid, @RequestParam String role) {
        Boolean success = iCSpringService.changeRole(userid, role);
        return success ? Result.success() : Result.error("修改失败");
    }

    //移除项目中的用户
    @DeleteMapping("/removeMember")
    public Result<Void> removeMember(@RequestParam Long userid) {
        Boolean success = iCSpringService.removeMember(userid);
        return success ? Result.success() : Result.error("移除失败");
    }

    //获取当前项目所有的用户列表
    @GetMapping("/getProjectMember")
    public Result<List<ProjectMember>> getProjectMember(Integer projectId) {
        List<ProjectMember> members = iCSpringService.getProjectMember(projectId);
        return Result.success(members);
    }

    //获取当前用户加入的项目列表
    @GetMapping("/myJoin")
    public Result<List<ProjectAdd>> getMyJoinedProjects(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        Long userid = userService.findUserIdByUsername(username);
        if (userid == null) {
            return Result.error("无效的用户标识");
        }
        return iCSpringService.getJoinedProjects(userid);
    }

    //用户退出项目
    @DeleteMapping("/leave")
    public Result<Void> leaveProject(@RequestBody LeaveProjectRequest request,
                                     Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        Long userid = userService.findUserIdByUsername(username);
        if (userid == null) {
            return Result.error("无效的用户标识");
        }
        return iCSpringService.leaveProject(userid, request.getProjectid());
    }

    //根据传入的用户名查询该用户创建的项目列表
    @GetMapping("/projectsByUsername")
    public Result<List<ProjectAdd>> getProjectsByUsername(@RequestParam String username) {
        return iCSpringService.getProjectsByUsername(username);
    }

    //当前登录用户通过项目名称加入项目
    @PostMapping("/joinByProjectName")
    public Result<Void> joinProjectByName(@RequestBody JoinProjectByNameRequest request,
                                          Authentication authentication) {
        String principal = (String) authentication.getPrincipal();
        Long userid;
        try {
            userid = Long.valueOf(principal);
        } catch (NumberFormatException e) {
            return Result.error("无效的用户标识");
        }
        return iCSpringService.joinProjectByName(userid, request.getProjectname());
    }

    //根据项目id获取项目详情
    @GetMapping("/project/{projectid}")
    public Result<ProjectAdd> getProjectDetail(@PathVariable Long projectid, Authentication authentication) {
        // 从 Authentication 中获取当前登录用户
        String username = (String) authentication.getPrincipal();
        Long userid = userService.findUserIdByUsername(username);
        if (userid == null) {
            return Result.error("无效的用户标识");
        }

        // 验证当前用户是否已加入该项目
        // if (!iCSpringService.isUserJoinedProject(userid, projectid)) {
        //     return Result.error("您尚未加入该项目，无法进入");
        // }

        // 获取项目详细信息
        return iCSpringService.getProjectById(projectid);
    }
}
