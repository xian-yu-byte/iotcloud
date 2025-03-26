package com.atguigu.cloud.iotcloudspring.pojo;

public class ProjectAdd {
    private Integer id;
    private String projectname;
    private String projectadministrator;
    private String region;
    private String projectdescription;

    public ProjectAdd() {
    }

    public ProjectAdd(Integer id, String projectname, String projectadministrator, String region, String projectdescription) {
        this.id = id;
        this.projectname = projectname;
        this.projectadministrator = projectadministrator;
        this.region = region;
        this.projectdescription = projectdescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectadministrator() {
        return projectadministrator;
    }

    public void setProjectadministrator(String projectadministrator) {
        this.projectadministrator = projectadministrator;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProjectdescription() {
        return projectdescription;
    }

    public void setProjectdescription(String projectdescription) {
        this.projectdescription = projectdescription;
    }
}
