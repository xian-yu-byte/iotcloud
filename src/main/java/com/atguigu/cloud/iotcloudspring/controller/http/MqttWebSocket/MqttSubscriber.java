package com.atguigu.cloud.iotcloudspring.controller.http.MqttWebSocket;

import com.atguigu.cloud.iotcloudspring.DTO.Device.Response.DeviceDetailResponse;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.mapper.MqttMapper;
import com.atguigu.cloud.iotcloudspring.pojo.Result;
import com.atguigu.cloud.iotcloudspring.pojo.device.Device;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceData;
import com.atguigu.cloud.iotcloudspring.pojo.device.DeviceTypeAttribute;
import com.atguigu.cloud.iotcloudspring.service.DeviceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class MqttSubscriber {

    private MqttClient client;

    @Resource
    private MqttMapper mqttMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private DeviceService deviceService;

    // 这里替换成你自己的 EMQX Broker 地址和端口，例如 tcp://broker.emqx.io:1883
    private final String brokerUrl = "ssl://vaa0511b.ala.cn-hangzhou.emqxsl.cn:8883";

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void init() {
        try {
            client = new MqttClient(brokerUrl, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置用户名和密码
            options.setUserName("ceshi");
            options.setPassword("123456".toCharArray());
//            options.setCleanSession(false);
//            options.setAutomaticReconnect(true);
//            options.setKeepAliveInterval(60);
            client.connect(options);
            System.out.println("MQTT 客户端已连接到 EMQX");
        } catch (MqttException e) {
            e.printStackTrace();
        }
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

                // ① 解析主题获取 deviceId
                Long deviceId = parseDeviceIdFromTopic(receivedTopic);
                if (deviceId <= 0) {
                    System.out.println("解析 deviceId 失败，跳过处理");
                    return;
                }

                // ② 使用 Jackson 解析 JSON 消息
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

                // ③ 查询 device 表，获取设备记录和 devicetypeid
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

                // ④ 遍历 JSON 对象中的每个字段
                // 使用 Jackson 迭代器遍历所有键值对
                try {
                    json.fields().forEachRemaining(entry -> {
                        String dataKey = entry.getKey();
                        String dataValue = entry.getValue().asText();

                        // 根据设备类型及属性名称查询 devicetypeattribute 表中对应的记录
                        DeviceTypeAttribute devTypeAttr = deviceMapper
                                .selectByTypeAndName(devicetypeid, dataKey);
                        if (devTypeAttr == null) {
                            System.out.println("未找到 devicetypeattribute，devicetypeid="
                                    + devicetypeid + ", attributename=" + dataKey + "，跳过该属性");
                            return;
                        }
                        DeviceData deviceData = new DeviceData();
                        deviceData.setDeviceid(deviceId);
                        deviceData.setDevicetypeattributeid(devTypeAttr.getId());
                        deviceData.setDatakey(dataKey);
                        deviceData.setDatavalue(dataValue);
                        deviceData.setTimestamp(LocalDateTime.now());
                        mqttMapper.insertEmqxDeviceData(deviceData);

                        System.out.println("已写入 devicedata：" + deviceData);
                    });
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