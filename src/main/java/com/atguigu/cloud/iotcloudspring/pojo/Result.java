package com.atguigu.cloud.iotcloudspring.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Result<T> {
    private Integer code;  // 响应码，1 代表成功，0 代表失败
    private String msg;  // 响应信息，描述字符串
    private T data;  // 返回的数据，使用泛型来确保类型安全

    public Result() {
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return new Result<>(1, "success", null);
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(1, "success", data);
    }

    // 失败响应
    public static <T> Result<T> error(String msg) {
        return new Result<>(0, msg, null);
    }
}
