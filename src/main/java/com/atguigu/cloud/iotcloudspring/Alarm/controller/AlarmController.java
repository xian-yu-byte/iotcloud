package com.atguigu.cloud.iotcloudspring.Alarm.controller;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.*;
import com.atguigu.cloud.iotcloudspring.Alarm.service.AlarmService;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
@AllArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/alarmRules")
    public Result<Long> create(@RequestBody @Valid AlarmRuleCreateDTO dto) {
        Long id = alarmService.createAlarmRule(dto);
        return Result.success(id);
    }

    @GetMapping("/alarmRules/{projectId}")
    public Result<List<AlarmRuleShowDTO>> show(@PathVariable("projectId") Long projectId) {
        List<AlarmRuleShowDTO> alarmRuleShowDTO = alarmService.showAlarmRule(projectId);
        return Result.success(alarmRuleShowDTO);
    }

    @GetMapping("/alarmRules/detail/{alarmId}")
    public Result<AlarmRuleDetailDTO> getDetail(@PathVariable("alarmId") Long alarmId) {
        AlarmRuleDetailDTO dto = alarmService.getAlarmRuleDetail(alarmId);
        if (dto == null) {
            return Result.error("该规则不存在");
        }
        return Result.success(dto);
    }

    @DeleteMapping("/alarmRules/delete/{alarmId}")
    public Result<Void> delAlarmRules(@PathVariable("alarmId") Long alarmId) {
        Boolean isSuccess = alarmService.isDelAlarmRules(alarmId);
        if (isSuccess) {
            return Result.success();
        } else return Result.error("删除失败");
    }

    @GetMapping("/alarmRules/logoInfoHistory/{alarmId}")
    public Result<List<AlarmLogoInfoHistoryDTO>> getLogoInfoHistory(@PathVariable("alarmId") Long alarmId) {
        List<AlarmLogoInfoHistoryDTO> list = alarmService.getLogoInfoHistory(alarmId);
        return Result.success(list);
    }

    @GetMapping("/alarmRules/fullHistory/{projectId}")
    public Result<List<AlarmLogoInfoHistorysDTO>> getFullHistoryByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<AlarmLogoInfoHistorysDTO> list = alarmService.getFullHistoryByProjectId(projectId);
        return Result.success(list);
    }

    @GetMapping("/alarmEvent/fullHistory")
    public Result<List<AlarmEventDTO>> getEvents(
            @RequestParam Long projectId
    ) {
        List<AlarmEventDTO> list = alarmService.listByProjectAndAttribute(projectId);
        return Result.success(list);
    }

    @PutMapping("/status/{id}")
    public Result<Void> clearEvent(@PathVariable("id") Long eventId) {
        boolean ok = alarmService.markCleared(eventId);
        if (ok) {
            return Result.success(null);
        } else {
            return Result.error("更新失败，找不到对应的告警事件");
        }
    }
}
