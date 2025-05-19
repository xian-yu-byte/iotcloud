package com.atguigu.cloud.iotcloudspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInvitation {
    private Long id;
    private Long projectId;
    private String inviter;
    private Long inviteeId;
    private String invitee;
    private String role;
    private LocalDateTime createdAt;
}
