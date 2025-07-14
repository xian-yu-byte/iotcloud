package com.atguigu.cloud.iotcloudspring.Rule.config;

import com.atguigu.cloud.iotcloudspring.Rule.mapper.CloudFunctionMapper;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.CloudFunction;
import com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket.MqttPublisher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FunctionExecutor {
    @Resource
    private CloudFunctionMapper functionMapper;
    @Resource
    private MqttPublisher mqttPublisher;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void execute(Long functionId, ObjectNode contextObj) {
        CloudFunction func = functionMapper.selectById(functionId);
//        if (func == null) {
//            throw new IllegalArgumentException("函数不存在 id=" + functionId);
//        }

        // 使用 try-with-resources 自动关闭 Context
        try (Context context = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .option("engine.WarnInterpreterOnly", "false")
                .build()) {
            // 注入触发上下文和安全 MQTT API
            context.getBindings("js").putMember("context", contextObj);
            MqttApi mqttApi = new MqttApi(mqttPublisher);
            context.getBindings("js").putMember("mqtt", mqttApi);

            // 执行脚本并调用 handler
            if (func != null) {
                context.eval("js", func.getScript());
                
                // 尝试执行handler函数
                Value handler = context.getBindings("js").getMember("handler");
                if (handler != null && !handler.isNull()) {
                    try {
                        handler.execute(contextObj);
                        return; // 如果handler执行成功，直接返回
                    } catch (Exception e) {
                        log.error("执行handler函数失败", e);
                        // 继续执行下面的直接发布逻辑
                    }
                }
            }
            
            // 如果func为null或者handler执行失败/不存在，直接从contextObj获取topic和payload发布消息
            if (contextObj != null) {
                String topic = null;
                String payload = null;
                
                // 尝试从contextObj获取topic和payload
                if (contextObj.has("topic")) {
                    topic = contextObj.get("topic").asText();
                }
                
                if (contextObj.has("payload")) {
                    JsonNode payloadNode = contextObj.get("payload");
                    if (payloadNode.isObject() || payloadNode.isArray()) {
                        try {
                            // 如果payload是JSON对象或数组，将其转换为JSON字符串
                            payload = objectMapper.writeValueAsString(payloadNode);
                        } catch (Exception e) {
                            log.error("将payload转换为JSON字符串时出错", e);
                            payload = payloadNode.toString(); // 备选方案
                        }
                    } else {
                        // 如果是简单值，直接获取文本
                        payload = payloadNode.asText();
                    }
                }
                
                System.out.println("从上下文获取数据:");
                System.out.println("topic = " + topic);
                System.out.println("payload = " + payload);
                
                // 如果有topic，则尝试发布消息
                if (topic != null && !topic.isEmpty()) {
                    mqttApi.publish(topic, payload);
                    log.info("通过上下文直接执行MQTT发布: topic={}, payload={}", topic, payload);
                } else {
                    log.warn("无法发布MQTT消息：上下文中缺少topic");
                }
            } else {
                log.warn("无法发布MQTT消息：上下文为空");
            }
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
            // 确保topic和payload不为null
            if (topic == null) {
                log.warn("MQTT发布失败: topic为null");
                return;
            }
            
            // 如果payload为null，使用空字符串代替
            String safePayload = payloadJson != null ? payloadJson : "";
            
            try {
                publisher.publish(topic, safePayload);
            } catch (Exception e) {
                log.error("MQTT发布异常: topic={}, payload={}", topic, safePayload, e);
            }
        }
    }
}