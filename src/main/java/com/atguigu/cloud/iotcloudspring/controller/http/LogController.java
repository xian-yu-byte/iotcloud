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
     * 发布新日志
     */
    @PostMapping
    public Result<Void> create(
            @RequestBody Map<String, String> body
    ) {
        String desc = body.get("description");
        if (desc == null || desc.isBlank()) {
            return Result.error("description 不能为空");
        }

        // 如果你有登录，userId 可以从 SecurityContext 里取
        Long userId = null;
        logService.addLog(userId, desc);
        return Result.success();
    }
}
