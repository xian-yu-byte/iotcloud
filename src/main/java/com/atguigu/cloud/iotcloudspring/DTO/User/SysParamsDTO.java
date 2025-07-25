package com.atguigu.cloud.iotcloudspring.DTO.User;

import com.atguigu.cloud.iotcloudspring.Common.group.AddGroup;
import com.atguigu.cloud.iotcloudspring.Common.group.DefaultGroup;
import com.atguigu.cloud.iotcloudspring.Common.group.UpdateGroup;
import com.atguigu.cloud.iotcloudspring.Common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema(description = "参数管理")
public class SysParamsDTO implements Serializable {

    @Schema(description = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @Schema(description = "参数编码")
    @NotBlank(message = "{sysparams.paramcode.require}", groups = DefaultGroup.class)
    private String paramCode;

    @Schema(description = "参数值")
    @NotBlank(message = "{sysparams.paramvalue.require}", groups = DefaultGroup.class)
    private String paramValue;

    @Schema(description = "值类型")
    @NotBlank(message = "{sysparams.valuetype.require}", groups = DefaultGroup.class)
    @Pattern(regexp = "^(string|number|boolean|array)$", message = "{sysparams.valuetype.pattern}", groups = DefaultGroup.class)
    private String valueType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createDate;

    @Schema(description = "更新时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date updateDate;

}

