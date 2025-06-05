package com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket;

import com.atguigu.cloud.iotcloudspring.Alarm.service.AlarmService;
import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceDetailResponse;
import com.atguigu.cloud.iotcloudspring.Rule.config.RuleEngine;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.mapper.MqttMapper;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceData;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceMessageLog;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceTypeAttribute;
import com.atguigu.cloud.iotcloudspring.pojo.mqtt.MqttTopicConfig;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.atguigu.cloud.iotcloudspring.service.MqttTopicConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MqttSubscriber {

    private MqttClient client;

    @Resource
    private MqttMapper mqttMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private DeviceService deviceService;
    @Resource
    private MqttTopicConfigService mqttTopicConfigService;
    @Resource
    private SimpMessagingTemplate wsTemplate;
    @Resource
    private RuleEngine ruleEngine;
    @Resource
    private AlarmService alarmService;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void init() {
        try {
            // 这里替换成你自己的 EMQX Broker 地址和端口，例如 tcp://broker.emqx.io:1883
            // private final String brokerUrl = "ssl://vaa0511b.ala.cn-hangzhou.emqxsl.cn:8883";
            String brokerUrl = "tcp://1.94.32.220:1883";
            client = new MqttClient(brokerUrl, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置用户名和密码
            options.setUserName("cloud");
            options.setPassword("123456".toCharArray());
//            options.setCleanSession(false);
//            options.setAutomaticReconnect(true);
//            options.setKeepAliveInterval(60);
            client.connect(options);
            System.out.println("MQTT 客户端已连接到 EMQX");

            loadSubscribeTopic();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void loadSubscribeTopic() {
        mqttTopicConfigService
        .getEffectiveTopics().forEach(
                c -> subscribeTopic(
                        "user" + c.getUserId()
                                + "/project" + c.getProjectId()
                                + "/device" + c.getDeviceId()
                                + "/" + c.getTopic()
                )
        );

    }

    /**
     * 订阅指定主题，当收到消息后通过 STOMP 推送给前端
     *
     * @param topic 例如 "user10/project17/device1/测试"
     */
    public void subscribeTopic(String topic) {
        try {
            client.subscribe(topic, (receivedTopic, message) -> {
                String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                System.out.println("收到 EMQX 消息，主题：" + receivedTopic + "，内容：" + payload);

                // 解析主题获取 deviceId
                Long deviceId = parseDeviceIdFromTopic(receivedTopic);
                if (deviceId <= 0) {
                    System.out.println("解析 deviceId 失败，跳过处理");
                    return;
                }

                // 使用 Jackson 解析 JSON 消息
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                JsonNode json;
                try {
                    json = objectMapper.readTree(payload);
                } catch (Exception e) {
                    System.out.println("JSON 解析失败: " + payload);
                    return;
                }

                // 查询 device 表，获取设备记录和 devicetypeid
                Device device = deviceMapper.selectDeviceById(deviceId);
                if (device == null) {
                    System.out.println("未找到对应的 device 记录，deviceId=" + deviceId);
                    return;
                }
                Long devicetypeid = device.getDevicetypeid();
                if (devicetypeid == null) {
                    System.out.println("deviceTypeId 为空，无法查找属性");
                    return;
                }

                LocalDateTime deviceTs = null;
                if (json.has("timestamp")) {
                    try {
                        deviceTs = LocalDateTime.parse(json.get("timestamp").asText());
                    } catch (DateTimeParseException e) {
                        log.warn("设备上报 timestamp 格式不对：{}", json.get("timestamp").asText());
                    }
                }

                // 遍历 JSON 对象中的每个字段
                // 使用 Jackson 迭代器遍历所有键值对
                Map<Long, BigDecimal> valueMap = new HashMap<>();
                try {
                    // 解析 JSON，把字段名转成 devTypeAttr.getId()，再放到 valueMap
                    json.fields().forEachRemaining(entry -> {
                        String dataKey = entry.getKey();
                        if ("timestamp".equals(dataKey)) return;
                        String dataValue = entry.getValue().asText();

                        DeviceTypeAttribute devTypeAttr = deviceMapper
                                .selectByTypeAndName(devicetypeid, dataKey);
                        log.info(">> selectByTypeAndName({}, \"{}\")", devicetypeid, dataKey);
                        if (devTypeAttr == null) return;

                        // 写入 DeviceData（原逻辑保留）
                        DeviceData deviceData = new DeviceData();
                        deviceData.setDeviceid(deviceId);
                        deviceData.setDevicetypeattributeid(devTypeAttr.getId());
                        deviceData.setDatakey(dataKey);
                        deviceData.setDatavalue(dataValue);
                        deviceData.setTimestamp(LocalDateTime.now());
                        mqttMapper.insertEmqxDeviceData(deviceData);

                        try {
                            BigDecimal value = new BigDecimal(dataValue);
                            valueMap.put(devTypeAttr.getId(), value);
                        } catch (NumberFormatException ignore) { }
                    });

                    log.info(">> valueMap = {}", valueMap);

                    // 插入日志 & 更新统计（原逻辑保留）
                    LocalDateTime receiveTs = LocalDateTime.now();
                    Long latencyMs = null;
                    if (deviceTs != null) {
                        latencyMs = Duration.between(deviceTs, receiveTs).toMillis();
                    }
                    DeviceMessageLog upLog = new DeviceMessageLog();
                    upLog.setDeviceId(deviceId);
                    upLog.setDirection("UP");
                    upLog.setTopic(receivedTopic);
                    upLog.setPayload(payload);
                    upLog.setCreatedAt(receiveTs);
                    if (latencyMs != null) {
                        upLog.setLatencyMs(latencyMs);
                    }
                    mqttMapper.insertMessageLog(upLog);
                    mqttMapper.upsertMessageStat(deviceId, 1, 0);

                    // 触发规则引擎（ID→数值）
                    if (!valueMap.isEmpty()) {
                        ruleEngine.evaluateBatch(deviceId, valueMap);
                        alarmService.evaluateBatch(deviceId, valueMap);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


//                messagingTemplate.convertAndSend("/topic/" + receivedTopic, payload);
                try {
                    DeviceDetailResponse detail = deviceService.getDeviceDetail(deviceId);
                    Result<DeviceDetailResponse> result = Result.success(detail);
                    String jsonResult = objectMapper.writeValueAsString(result);
                    messagingTemplate.convertAndSend("/topic/detail/" + deviceId, jsonResult);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            System.out.println("成功订阅主题：" + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从主题中解析 deviceId，
     * 假设第三段是形如 "device1" => 返回 int 1
     * 若无法解析则返回 -1 或 0 表示失败
     */
    private Long parseDeviceIdFromTopic(String topic) {
        String[] parts = topic.split("/");
        if (parts.length < 3) {
            return (long) -1;
        }
        String deviceStr = parts[2].replace("device", "");
        try {
            return Long.parseLong(deviceStr);
        } catch (NumberFormatException e) {
            return (long) -1;
        }
    }

    public void unsubscribeTopic(String topic) throws MqttException {
        client.unsubscribe(topic);
        System.out.println("成功取消订阅主题：" + topic);
    }
}