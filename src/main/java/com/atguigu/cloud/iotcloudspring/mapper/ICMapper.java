package com.atguigu.cloud.iotcloudspring.mapper;

import com.atguigu.cloud.iotcloudspring.VO.ProjectMember;
import com.atguigu.cloud.iotcloudspring.pojo.ProjectAdd;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.User.UserProject;
import com.atguigu.cloud.iotcloudspring.pojo.User.users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ICMapper {

    //获取projectadd的所有数据
    List<ProjectAdd> GetProject(String username);

    //添加projectadd的数据
    Long AddProject(ProjectAdd projectAdd);

    //查询用户名是否存在
    users selectUserByUsername(String username);

    //注册用户
    Long RegisteredUser(users user);

    //修改密码
    Long updatePassword(@Param("username") String username, @Param("newPassword") String newPassword);

    // 插入用户-项目关联记录
    Long insertUserProject(UserProject userProject);

    // 根据用户ID查询该用户加入的所有项目关联记录
    List<UserProject> selectByUserId(@Param("userid") Long userid);

    // 根据项目创建者（admin 字段）查询该用户创建的所有项目
    List<ProjectAdd> selectProjectsByAdmin(@Param("projectadministrator") String projectadministrator);

    // 根据项目名称查询项目
    ProjectAdd selectProjectByName(@Param("projectname") String projectname);

    // 删除用户-项目关联记录（退出项目）
    Long deleteUserProject(@Param("userid") Long userid, @Param("projectid") Long projectid);

    // 根据项目ID查询项目
    List<ProjectAdd> selectJoinedProjectsByUser(@Param("userid") Long userid);

    // 根据用户ID和项目ID查询用户-项目关联记录
    UserProject selectUserProjectByUserIdAndProjectId(@Param("userid") Long userid,
                                                      @Param("projectid") Long projectid);

    // 根据项目ID查询项目
    ProjectAdd selectProjectById(@Param("id") Long id);

    // 邀请用户加入项目
    Boolean addUserProject(Long userid, Long projectid, String role);

    //获取当前项目所有的用户列表
    List<ProjectMember> getProjectMember(Integer projectId);

    //修改用户角色
    Long changeRole(Long userid, String role);

    //移除项目成员
    Long removeMember(Long userid);
}
