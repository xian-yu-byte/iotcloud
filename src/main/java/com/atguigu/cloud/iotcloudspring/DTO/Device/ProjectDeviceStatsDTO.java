package com.atguigu.cloud.iotcloudspring.DTO.Device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDeviceStatsDTO {
    private Long   projectId;
    private Integer totalDevices;
    private Integer onlineDevices;
    private Integer offlineDevices;
    private List<EnumCount> accessCategoryStats;
    private List<EnumCount> communicationModeStats;
}
