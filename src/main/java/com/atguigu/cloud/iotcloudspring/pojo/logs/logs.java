package com.atguigu.cloud.iotcloudspring.pojo.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class logs {
    private Long id;
    private String username;
    private String description;
    private LocalDateTime createtime;
}
