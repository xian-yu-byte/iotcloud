package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.Common.constant.Constant;
import com.atguigu.cloud.iotcloudspring.Common.exception.RenException;
import com.atguigu.cloud.iotcloudspring.Common.utils.ResultConfig;
import com.atguigu.cloud.iotcloudspring.Common.utils.ValidatorUtils;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AgentModelsDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Config.ConfigSecretDTO;
import com.atguigu.cloud.iotcloudspring.DTO.IdDTO;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.service.ConfigService;
import com.atguigu.cloud.iotcloudspring.service.SysParamsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
@AllArgsConstructor
public class ConfigController {
    private final ConfigService configService;
    private final SysParamsService sysParamsService;

    @PostMapping("server-base")
    @Operation(summary = "获取配置")
    public ResultConfig<Object> getConfig(@RequestBody ConfigSecretDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto);
        checkSecret(dto.getSecret());
        Object config = configService.getConfig(true);
        return new ResultConfig<Object>().ok(config);
    }

    @PostMapping("agent-models")
    @Operation(summary = "获取智能体模型")
    public ResultConfig<Object> getAgentModels(@RequestBody AgentModelsDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto);
        checkSecret(dto.getSecret());
        Object models = configService.getAgentModels(dto.getMacAddress(), dto.getSelectedModule());
        return new ResultConfig<Object>().ok(models);
    }

    private void checkSecret(String secret) {
        String secretParam = sysParamsService.getValue(Constant.SERVER_SECRET, true);
        // 验证密钥
        if (StringUtils.isBlank(secret) || !secret.equals(secretParam)) {
            throw new RenException("密钥错误");
        }
    }

    @PostMapping("devicekey/{devicekey}")
    @Operation(summary = "根据设备key所有基础信息")
    public Result<IdDTO> getIdByDeviceKey(@PathVariable("devicekey") String devicekey) {
        IdDTO id = configService.getById(devicekey);
        return Result.success(id);
    }
}
