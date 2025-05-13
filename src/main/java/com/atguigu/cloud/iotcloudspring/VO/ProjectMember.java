package com.atguigu.cloud.iotcloudspring.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {
    private Long userid;
    private String username;
    private String role;
}
