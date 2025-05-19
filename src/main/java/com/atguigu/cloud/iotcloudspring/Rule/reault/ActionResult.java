package com.atguigu.cloud.iotcloudspring.Rule.reault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResult {
    private boolean allSuccess;
    private List<String> errorMessages;
}
