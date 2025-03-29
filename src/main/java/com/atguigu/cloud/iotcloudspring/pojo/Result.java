package com.atguigu.cloud.iotcloudspring.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;  // 响应码，1 代表成功，0 代表失败
    private String msg;  // 响应信息，描述字符串
    private T data;  // 返回的数据，使用泛型来确保类型安全

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
