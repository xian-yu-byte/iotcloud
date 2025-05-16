package com.atguigu.cloud.iotcloudspring.Rule.controller;

import com.atguigu.cloud.iotcloudspring.Rule.mapper.CloudFunctionMapper;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.CloudFunction;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/IC/functions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CloudFunctionController {

    private final CloudFunctionMapper fnMapper;

    // 创建函数
    @PostMapping
    public Result<Long> create(@RequestBody CloudFunction fn) {
        fn.setRuntime("js");
        fnMapper.insert(fn);
        return Result.success(fn.getId());
    }

    // 更新函数
    @PutMapping("{id}")
    public Result<Void> update(@PathVariable Long id,
                               @RequestBody CloudFunction fn) {
        fn.setId(id);
        fnMapper.updateById(fn);
        return Result.success();
    }

    // 获取函数
    @GetMapping
    public Result<List<CloudFunction>> list() {
        return Result.success(fnMapper.selectList(null));
    }
}
