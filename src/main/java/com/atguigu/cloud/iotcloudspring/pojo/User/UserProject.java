package com.atguigu.cloud.iotcloudspring.pojo.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProject {
    private Long id;
    private Long userid;
    private Long projectid;
    private String role;
    private LocalDateTime joinedat;
}
