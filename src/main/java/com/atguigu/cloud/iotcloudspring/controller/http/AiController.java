package com.atguigu.cloud.iotcloudspring.controller.http;

import com.atguigu.cloud.iotcloudspring.Common.constant.Constant;
import com.atguigu.cloud.iotcloudspring.Common.page.PageData;
import com.atguigu.cloud.iotcloudspring.Common.service.CommonService;
import com.atguigu.cloud.iotcloudspring.Common.utils.ConvertUtils;
import com.atguigu.cloud.iotcloudspring.Common.utils.ValidatorUtils;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.*;
import com.atguigu.cloud.iotcloudspring.VO.TimbreDetailsVO;
import com.atguigu.cloud.iotcloudspring.config.SecurityUser;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgent;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgentTemplate;
import com.atguigu.cloud.iotcloudspring.service.AgentService;
import com.atguigu.cloud.iotcloudspring.service.AgentTemplateService;
import com.atguigu.cloud.iotcloudspring.service.TimbreService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/IC")
@CrossOrigin("*")
@AllArgsConstructor
public class AiController {

    @Resource
    private AgentService agentService;
    @Resource
    private CommonService commonService;
    @Resource
    private AgentTemplateService agentTemplateService;
    @Resource
    private TimbreService timbreService;
    @Resource
    private SecurityUser securityUser;

    @GetMapping("/project/{projectId}/list")
    @Operation(summary = "按项目获取智能体列表")
    public Result<List<AgentDTO>> getAgentsByProject(
            @PathVariable("projectId") Long projectId) {
        List<AgentDTO> list = agentService.getAgentsByProjectId(projectId);
        return Result.success(list);
    }

    @PostMapping("/createAi")
    @Operation(summary = "创建智能体")
    public Result<Void> save(@RequestBody @Valid AgentCreateDTO dto) {
        AiAgent entity = ConvertUtils.sourceToTarget(dto, AiAgent.class);

        // 获取默认模板
        AiAgentTemplate template = agentTemplateService.getDefaultTemplate();
        if (template != null) {
            // 设置模板中的默认值
            entity.setProjectId(dto.getProjectId());
            entity.setAsrModelId(template.getAsrModelId());
            entity.setVadModelId(template.getVadModelId());
            entity.setLlmModelId(template.getLlmModelId());
            entity.setTtsModelId(template.getTtsModelId());
            entity.setTtsVoiceId(template.getTtsVoiceId());
            entity.setMemModelId(template.getMemModelId());
            entity.setIntentModelId(template.getIntentModelId());
            entity.setSystemPrompt(template.getSystemPrompt());
            entity.setLangCode(template.getLangCode());
            entity.setLanguage(template.getLanguage());
        }

        // 设置用户ID和创建者信息
        Long userId = securityUser.getUser();
        entity.setUserId(userId);
        entity.setCreator(userId);
        entity.setCreatedAt(new Date());

        // ID、智能体编码和排序会在Service层自动生成
        agentService.createAgent(entity);

        return new Result<>();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取智能体详情")
    public Result<AiAgent> getAgentById(@PathVariable("id") String id) {
        AiAgent agent = agentService.getAgentById(id);
        return Result.success(agent);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "更新智能体")
    public Result<Void> update(@PathVariable String id, @RequestBody @Valid AgentUpdateDTO dto) {
        // 先查询现有实体
        AiAgent existingEntity = agentService.getAgentById(id);
        if (existingEntity == null) {
            return new Result<Void>().error("智能体不存在");
        }

        // 只更新提供的非空字段
        if (dto.getAgentName() != null) {
            existingEntity.setAgentName(dto.getAgentName());
        }
        if (dto.getAgentCode() != null) {
            existingEntity.setAgentCode(dto.getAgentCode());
        }
        if (dto.getAsrModelId() != null) {
            existingEntity.setAsrModelId(dto.getAsrModelId());
        }
        if (dto.getVadModelId() != null) {
            existingEntity.setVadModelId(dto.getVadModelId());
        }
        if (dto.getLlmModelId() != null) {
            existingEntity.setLlmModelId(dto.getLlmModelId());
        }
        if (dto.getTtsModelId() != null) {
            existingEntity.setTtsModelId(dto.getTtsModelId());
        }
        if (dto.getTtsVoiceId() != null) {
            existingEntity.setTtsVoiceId(dto.getTtsVoiceId());
        }
        if (dto.getMemModelId() != null) {
            existingEntity.setMemModelId(dto.getMemModelId());
        }
        if (dto.getIntentModelId() != null) {
            existingEntity.setIntentModelId(dto.getIntentModelId());
        }
        if (dto.getSystemPrompt() != null) {
            existingEntity.setSystemPrompt(dto.getSystemPrompt());
        }
        if (dto.getLangCode() != null) {
            existingEntity.setLangCode(dto.getLangCode());
        }
        if (dto.getLanguage() != null) {
            existingEntity.setLanguage(dto.getLanguage());
        }
        if (dto.getSort() != null) {
            existingEntity.setSort(dto.getSort());
        }

        // 设置更新者信息
        Long userId = securityUser.getUser();
        existingEntity.setUpdater(userId);
        existingEntity.setUpdatedAt(new Date());

        commonService.updateById(existingEntity);

        return new Result<>();
    }

    @GetMapping("/template")
    @Operation(summary = "智能体模板模板列表")
    public Result<List<AiAgentTemplate>> templateList() {
        List<AiAgentTemplate> list = agentTemplateService
                .list(new QueryWrapper<AiAgentTemplate>().orderByAsc("sort"));
        return Result.success(list);
    }

    @GetMapping("/{modelId}/voices")
    @Operation(summary = "获取模型音色")
    public Result<List<VoiceDTO>> getVoiceList(@PathVariable String modelId,
                                               @RequestParam(required = false) String voiceName) {
        List<VoiceDTO> voiceList = timbreService.getVoiceNames(modelId, voiceName);
        return Result.success(voiceList);
    }

    @GetMapping("/ttl/page")
    @Operation(summary = "分页查找")
    @Parameters({
            @Parameter(name = "ttsModelId", description = "对应 TTS 模型主键", required = true),
            @Parameter(name = "name", description = "音色名称"),
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", required = true),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", required = true),
    })
    public Result<PageData<TimbreDetailsVO>> page(
            @Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        TimbrePageDTO dto = new TimbrePageDTO();
        dto.setTtsModelId((String) params.get("ttsModelId"));
        dto.setName((String) params.get("name"));
        dto.setLimit((String) params.get(Constant.LIMIT));
        dto.setPage((String) params.get(Constant.PAGE));

        ValidatorUtils.validateEntity(dto);
        PageData<TimbreDetailsVO> page = timbreService.page(dto);
        return Result.success(page);
    }
}
