package com.atguigu.cloud.iotcloudspring.Alarm.controller;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleCreateDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleDetailDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleShowDTO;
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
}
