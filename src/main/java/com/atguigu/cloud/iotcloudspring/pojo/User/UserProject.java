package com.atguigu.cloud.iotcloudspring.pojo.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProject {
    private Integer id;
    private Integer userid;
    private Integer projectid;
    private String role;
    private LocalDateTime joinedat;
}
