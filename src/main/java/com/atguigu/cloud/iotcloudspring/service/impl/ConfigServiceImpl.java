package com.atguigu.cloud.iotcloudspring.service.impl;

import com.atguigu.cloud.iotcloudspring.Common.exception.ErrorCode;
import com.atguigu.cloud.iotcloudspring.Common.exception.RenException;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisKeys;
import com.atguigu.cloud.iotcloudspring.Common.redis.RedisUtils;
import com.atguigu.cloud.iotcloudspring.Common.utils.JsonUtils;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceTypeAttributeResponse;
import com.atguigu.cloud.iotcloudspring.DTO.IdDTO;
import com.atguigu.cloud.iotcloudspring.DTO.User.SysParamsDTO;
import com.atguigu.cloud.iotcloudspring.VO.TimbreDetailsVO;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.pojo.Model.AiModelConfig;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgent;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiAgentTemplate;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiDevice;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceTypeAttribute;
import com.atguigu.cloud.iotcloudspring.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@AllArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final SysParamsService sysParamsService;
    private final AiDeviceService aiDeviceService;
    private final ModelConfigService modelConfigService;
    private final AgentService agentService;
    private final AgentTemplateService agentTemplateService;
    private final RedisUtils redisUtils;
    private final TimbreService timbreService;
    private final DeviceMapper deviceMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Object getConfig(Boolean isCache) {
        if (isCache) {
            // 先从Redis获取配置
            Object cachedConfig = redisUtils.get(RedisKeys.getServerConfigKey());
            if (cachedConfig != null) {
                return cachedConfig;
            }
        }

        // 构建配置信息
        Map<String, Object> result = new HashMap<>();
        buildConfig(result);

        // 查询默认智能体
        AiAgentTemplate agent = agentTemplateService.getDefaultTemplate();
        if (agent == null) {
            throw new RenException("默认智能体未找到");
        }

        // 构建模块配置
        buildModuleConfig(
                agent.getAgentName(),
                agent.getSystemPrompt(),
                null,
                agent.getVadModelId(),
                agent.getAsrModelId(),
                agent.getLlmModelId(),
                agent.getTtsModelId(),
                agent.getMemModelId(),
                agent.getIntentModelId(),
                result,
                isCache);

        // 将配置存入Redis
        redisUtils.set(RedisKeys.getServerConfigKey(), result);

        return result;
    }

    @Override
    public Map<String, Object> getAgentModels(String macAddress, Map<String, String> selectedModule) {
        // 根据MAC地址查找设备
        AiDevice device = aiDeviceService.getDeviceByMacAddress(macAddress);
        if (device == null) {
            // 如果设备，去redis里看看有没有需要连接的设备
            String cachedCode = aiDeviceService.geCodeByDeviceId(macAddress);
            if (StringUtils.isNotBlank(cachedCode)) {
                throw new RenException(ErrorCode.OTA_DEVICE_NEED_BIND, cachedCode);
            }
            throw new RenException(ErrorCode.OTA_DEVICE_NOT_FOUND, "not found device");
        }

        // 获取智能体信息
        AiAgent agent = agentService.getAgentById(device.getAgentId());
        if (agent == null) {
            throw new RenException("智能体未找到");
        }
        // 获取音色信息
        String voice = null;
        TimbreDetailsVO timbre = timbreService.get(agent.getTtsVoiceId());
        if (timbre != null) {
            voice = timbre.getTtsVoice();
        }
        // 构建返回数据
        Map<String, Object> result = new HashMap<>();
        // 获取单台设备每天最多输出字数
        String deviceMaxOutputSize = sysParamsService.getValue("device_max_output_size", true);
        result.put("device_max_output_size", deviceMaxOutputSize);
        // 如果客户端已实例化模型，则不返回
        String alreadySelectedVadModelId = (String) selectedModule.get("VAD");
        if (alreadySelectedVadModelId != null && alreadySelectedVadModelId.equals(agent.getVadModelId())) {
            agent.setVadModelId(null);
        }
        String alreadySelectedAsrModelId = (String) selectedModule.get("ASR");
        if (alreadySelectedAsrModelId != null && alreadySelectedAsrModelId.equals(agent.getAsrModelId())) {
            agent.setAsrModelId(null);
        }
        String alreadySelectedLlmModelId = (String) selectedModule.get("LLM");
        if (alreadySelectedLlmModelId != null && alreadySelectedLlmModelId.equals(agent.getLlmModelId())) {
            agent.setLlmModelId(null);
        }
        String alreadySelectedMemModelId = (String) selectedModule.get("Memory");
        if (alreadySelectedMemModelId != null && alreadySelectedMemModelId.equals(agent.getMemModelId())) {
            agent.setMemModelId(null);
        }
        String alreadySelectedIntentModelId = (String) selectedModule.get("Intent");
        if (alreadySelectedIntentModelId != null && alreadySelectedIntentModelId.equals(agent.getIntentModelId())) {
            agent.setIntentModelId(null);
        }

        // 构建模块配置
        buildModuleConfig(
                agent.getAgentName(),
                agent.getSystemPrompt(),
                voice,
                agent.getVadModelId(),
                agent.getAsrModelId(),
                agent.getLlmModelId(),
                agent.getTtsModelId(),
                agent.getMemModelId(),
                agent.getIntentModelId(),
                result,
                true);

        return result;
    }

    /**
     * 构建配置信息
     *
     * @return 配置信息
     */
    @SuppressWarnings("unchecked")
    private Object buildConfig(Map<String, Object> config) {

        // 查询所有系统参数
        List<SysParamsDTO> paramsList = sysParamsService.list(new HashMap<>());

        for (SysParamsDTO param : paramsList) {
            String[] keys = param.getParamCode().split("\\.");
            Map<String, Object> current = config;

            // 遍历除最后一个key之外的所有key
            for (int i = 0; i < keys.length - 1; i++) {
                String key = keys[i];
                if (!current.containsKey(key)) {
                    current.put(key, new HashMap<String, Object>());
                }
                current = (Map<String, Object>) current.get(key);
            }

            // 处理最后一个key
            String lastKey = keys[keys.length - 1];
            String value = param.getParamValue();

            // 根据valueType转换值
            switch (param.getValueType().toLowerCase()) {
                case "number":
                    try {
                        double doubleValue = Double.parseDouble(value);
                        // 如果数值是整数形式，则转换为Integer
                        if (doubleValue == (int) doubleValue) {
                            current.put(lastKey, (int) doubleValue);
                        } else {
                            current.put(lastKey, doubleValue);
                        }
                    } catch (NumberFormatException e) {
                        current.put(lastKey, value);
                    }
                    break;
                case "boolean":
                    current.put(lastKey, Boolean.parseBoolean(value));
                    break;
                case "array":
                    // 将分号分隔的字符串转换为数字数组
                    List<String> list = new ArrayList<>();
                    for (String num : value.split(";")) {
                        if (StringUtils.isNotBlank(num)) {
                            list.add(num.trim());
                        }
                    }
                    current.put(lastKey, list);
                    break;
                case "json":
                    try {
                        current.put(lastKey, JsonUtils.parseObject(value, Object.class));
                    } catch (Exception e) {
                        current.put(lastKey, value);
                    }
                    break;
                default:
                    current.put(lastKey, value);
            }
        }

        return config;
    }

    /**
     * 构建模块配置
     * 
     * @param prompt        提示词
     * @param voice         音色
     * @param vadModelId    VAD模型ID
     * @param asrModelId    ASR模型ID
     * @param llmModelId    LLM模型ID
     * @param ttsModelId    TTS模型ID
     * @param memModelId    记忆模型ID
     * @param intentModelId 意图模型ID
     * @param result        结果Map
     */
    private void buildModuleConfig(
            String assistantName,
            String prompt,
            String voice,
            String vadModelId,
            String asrModelId,
            String llmModelId,
            String ttsModelId,
            String memModelId,
            String intentModelId,
            Map<String, Object> result,
            boolean isCache) {
        Map<String, String> selectedModule = new HashMap<>();

        String[] modelTypes = { "VAD", "ASR", "TTS", "Memory", "Intent", "LLM" };
        String[] modelIds = { vadModelId, asrModelId, ttsModelId, memModelId, intentModelId, llmModelId };
        String intentLLMModelId = null;

        for (int i = 0; i < modelIds.length; i++) {
            if (modelIds[i] == null) {
                continue;
            }
            AiModelConfig model = modelConfigService.getModelById(modelIds[i], isCache);
            Map<String, Object> typeConfig = new HashMap<>();
            JsonNode configJson = model.getConfigJson();
            if (model.getConfigJson() != null) {
                Map<String, Object> map = objectMapper.convertValue(
                        configJson,
                        new TypeReference<Map<String, Object>>() {}
                );
                // 如果是TTS类型，添加private_voice属性
                if ("TTS".equals(modelTypes[i]) && voice != null) {
                    map.put("private_voice", voice);
                }
                // 如果是Intent类型，且type=intent_llm，则给他添加附加模型
                if ("Intent".equals(modelTypes[i])) {
                    String type = (String) map.get("type");
                    if ("intent_llm".equals(type)) {
                        intentLLMModelId = (String) map.get("llm");
                        if (intentLLMModelId != null && intentLLMModelId.equals(llmModelId)) {
                            intentLLMModelId = null;
                        }
                    } else if ("function_call".equals(type)) {
                        String functionStr = (String) map.get("functions");
                        if (StringUtils.isNotBlank(functionStr)) {
                            map.put("functions", functionStr.split("\\;"));
                        }
                    }
                }
                typeConfig.put(model.getId(), map);
                // 如果是LLM类型，且intentLLMModelId不为空，则添加附加模型
                if ("LLM".equals(modelTypes[i]) && intentLLMModelId != null) {
                    AiModelConfig intentLLM = modelConfigService.getModelById(intentLLMModelId, isCache);
                    JsonNode iiJson = intentLLM.getConfigJson();
                    if (iiJson != null && iiJson.isObject()) {
                        Map<String, Object> iiMap = objectMapper.convertValue(
                                iiJson,
                                new TypeReference<Map<String, Object>>() {}
                        );
                        typeConfig.put(intentLLM.getId(), iiMap);
                    }
                }
            } else {
                // 没有配置 JSON 的情况下，也保证 key 存在
                typeConfig.put(model.getId(), Collections.emptyMap());
            }
            result.put(modelTypes[i], typeConfig);

            selectedModule.put(modelTypes[i], model.getId());
        }

        result.put("selected_module", selectedModule);
        if (StringUtils.isNotBlank(prompt)) {
            prompt = prompt.replace("{{assistant_name}}", "小智");
        }
        result.put("prompt", prompt);
    }

    @Override
    public IdDTO getById(String deviceKey) {
        // ① 查设备基础信息
        IdDTO info = deviceMapper.selectDeviceKeyById(deviceKey);
        // ② 查该设备类型的所有属性
        List<DeviceTypeAttribute> attrs =
                deviceMapper.selectAttributesByDeviceTypeId(info.getDevicetypeid());
        // ③ 填充到 DTO
        info.setAttributes(attrs);
        return info;
    }
}
