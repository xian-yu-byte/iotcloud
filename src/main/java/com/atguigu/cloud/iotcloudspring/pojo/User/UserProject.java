package com.atguigu.cloud.iotcloudspring.pojo.User;

import java.time.LocalDateTime;

public class UserProject {
    private Integer id;
    private Integer userid;
    private Integer projectid;
    private String role;
    private LocalDateTime joinedat;

    public UserProject() {
    }

    public UserProject(Integer id, Integer userid, Integer projectid, String role, LocalDateTime joinedat) {
        this.id = id;
        this.userid = userid;
        this.projectid = projectid;
        this.role = role;
        this.joinedat = joinedat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedat() {
        return joinedat;
    }

    public void setJoinedat(LocalDateTime joinedat) {
        this.joinedat = joinedat;
    }
}
