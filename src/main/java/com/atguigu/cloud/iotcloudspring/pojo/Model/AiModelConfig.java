package com.atguigu.cloud.iotcloudspring.pojo.Model;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "ai_model_config", autoResultMap = true)
@Schema(description = "模型配置表")
public class AiModelConfig {
    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "主键")
    private String id;

    @Schema(description = "模型类型(Memory/ASR/VAD/LLM/TTS)")
    private String modelType;

    @Schema(description = "模型编码(如AliLLM、DoubaoTTS)")
    private String modelCode;

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "是否默认配置(0否 1是)")
    private Integer isDefault;

    @Schema(description = "是否启用")
    private Integer isEnabled;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "模型配置(JSON格式)")
    private JsonNode  configJson;

    @Schema(description = "官方文档链接")
    private String docLink;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "更新者")
    @TableField(fill = FieldFill.UPDATE)
    private Long updater;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateDate;

    @Schema(description = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;
}
