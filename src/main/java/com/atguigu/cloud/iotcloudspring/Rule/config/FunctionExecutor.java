package com.atguigu.cloud.iotcloudspring.Rule.config;

import com.atguigu.cloud.iotcloudspring.Rule.mapper.CloudFunctionMapper;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.CloudFunction;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttPublisher;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

@Service
public class FunctionExecutor {
    @Resource
    private CloudFunctionMapper functionMapper;
    @Resource
    private MqttPublisher mqttPublisher;

    public void execute(Long functionId, ObjectNode contextObj) {
        CloudFunction func = functionMapper.selectById(functionId);
        if (func == null) {
            throw new IllegalArgumentException("函数不存在 id=" + functionId);
        }

        // 使用 try-with-resources 自动关闭 Context
        try (Context context = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .option("engine.WarnInterpreterOnly", "false")
                .build()) {
            // 注入触发上下文和安全 MQTT API
            context.getBindings("js").putMember("context", contextObj);
            context.getBindings("js").putMember("mqtt", new MqttApi(mqttPublisher));

            // 执行脚本并调用 handler
            context.eval("js", func.getScript());
            Value handler = context.getBindings("js").getMember("handler");
            handler.execute(contextObj);
        }
    }

    /**
     * 仅暴露 publish(topic, payloadJson) 给脚本使用
     */
    public static class MqttApi {
        private final MqttPublisher publisher;

        public MqttApi(MqttPublisher publisher) {
            this.publisher = publisher;
        }

        public void publish(String topic, String payloadJson) {
            publisher.publish(topic, payloadJson);
        }
    }
}