package com.atguigu.cloud.iotcloudspring.Rule.controller;

import com.atguigu.cloud.iotcloudspring.Rule.DTO.CreateRuleDTO;
import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleCardDTO;
import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleDeviceDTO;
import com.atguigu.cloud.iotcloudspring.Rule.DTO.RuleTargetsDTO;
import com.atguigu.cloud.iotcloudspring.Rule.service.RuleService;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
public class RuleController {
    @Resource
    private RuleService ruleService;

    // 插入内容
    @PostMapping("/rule")
    public Result<Long> createRule(@RequestBody CreateRuleDTO dto) {
        Long ruleId = ruleService.createRule(dto);
        return Result.success(ruleId);
    }

    // 查询内容
    @GetMapping("{projectId}/ruleCard")
    public Result<List<RuleCardDTO>> getRuleCard(@PathVariable Long projectId) {
        List<RuleCardDTO> ruleCardDTOList = ruleService.getRuleCard(projectId);
        return Result.success(ruleCardDTOList);
    }

    // 是否启用规则
    @GetMapping("{ruleId}/enable")
    public Result<Boolean> enableRule(@PathVariable Long ruleId,
                                      @RequestParam boolean enabled) {
        Boolean isEnable = ruleService.updateEnableRule(ruleId, enabled);
        return Result.success(isEnable);
    }

    // 删除规则
    @DeleteMapping("{ruleId}/delRule")
    public Result<Boolean> delRule(@PathVariable Long ruleId) {
        Boolean isDel = ruleService.delRule(ruleId);
        return Result.success(isDel);
    }

    @GetMapping("/project/{projectId}/targets/{ruleId}")
    public Result<List<RuleTargetsDTO>> getTargets(@PathVariable Long projectId, @PathVariable Long ruleId) {
        List<RuleTargetsDTO> list = ruleService.listByProject(projectId,ruleId);
        return Result.success(list);
    }

    @GetMapping("/rules/{ruleId}/deviceNames")
    public Result<List<RuleDeviceDTO>> listDeviceNames(@PathVariable Long ruleId) {
        List<RuleDeviceDTO> list = ruleService.getDevicesWithEnable(ruleId);
        return Result.success(list);
    }
}
