package com.atguigu.cloud.iotcloudspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAdd {
    private Long id;
    private Long userid;
    private String projectname;
    private String projectadministrator;
    private String region;
    private String projectdescription;
}
