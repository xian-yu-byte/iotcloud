package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.logs.logs;
import com.atguigu.cloud.iotcloudspring.service.LogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/IC/logs")
@CrossOrigin("*")
public class LogController {
    @Resource
    private LogService logService;

    /**
     * 分页查询日志
     */
    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<logs> records = logService.listLogs(page, size);
        long total = logService.countLogs();

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", total);
        return Result.success(data);
    }

    /**
     * 根据ID查询日志
     */
    @GetMapping("/{id}")
    public Result<logs> getById(@PathVariable Long id) {
        logs log = logService.getLogById(id);
        if (log == null) {
            return Result.error("日志不存在");
        }
        return Result.success(log);
    }

    /**
     * 添加日志
     */
    @PostMapping
    public Result<Void> create(@RequestBody logs log) {
        String username = log.getUsername();
        String desc = log.getDescription();
        
        if (desc == null || desc.isBlank()) {
            return Result.error("description 不能为空");
        }
        
        if (username == null || username.isBlank()) {
            return Result.error("username 不能为空");
        }

        logService.addLog(username, desc);
        return Result.success();
    }
    
    /**
     * 更新日志
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody logs log) {
        log.setId(id);
        boolean success = logService.updateLog(log);
        
        if (!success) {
            return Result.error("更新失败，日志可能不存在");
        }
        
        return Result.success();
    }
    
    /**
     * 删除日志
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = logService.deleteLog(id);
        
        if (!success) {
            return Result.error("删除失败，日志可能不存在");
        }
        
        return Result.success();
    }
}
