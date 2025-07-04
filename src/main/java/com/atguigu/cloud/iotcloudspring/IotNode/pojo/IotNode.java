package com.atguigu.cloud.iotcloudspring.IotNode.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IotNode {
    private Long id;
    private Long projectId;
    private Long parentId;
    private String nodeType;
    private String nodeName;
    private String description;
    private String additionalInfo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
