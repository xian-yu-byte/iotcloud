package com.atguigu.cloud.iotcloudspring.Alarm.controller;

import com.atguigu.cloud.iotcloudspring.Alarm.DTO.AlarmRuleCreateDTO;
import com.atguigu.cloud.iotcloudspring.Alarm.service.AlarmService;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
