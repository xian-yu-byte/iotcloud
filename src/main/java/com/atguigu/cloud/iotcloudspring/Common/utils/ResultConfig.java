package com.atguigu.cloud.iotcloudspring.Common.utils;

import com.atguigu.cloud.iotcloudspring.Common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "响应")
public class ResultConfig<T> implements Serializable {
    /**
     * 编码：0表示成功，其他值表示失败
     */
    @Schema(description = "编码：0表示成功，其他值表示失败")
    private int code = 0;
    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String msg = "success";
    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    public ResultConfig<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public ResultConfig<T> error() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public ResultConfig<T> error(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public ResultConfig<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ResultConfig<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }
}
