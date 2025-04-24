package com.atguigu.cloud.iotcloudspring.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.atguigu.cloud.iotcloudspring.Common.service.impl.BaseServiceImpl;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AiDevice.DeviceReportReqDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.AiDevice.DeviceReportRespDTO;
import com.atguigu.cloud.iotcloudspring.mapper.AiDeviceMapper;
import com.atguigu.cloud.iotcloudspring.pojo.ai.AiDevice;
import com.atguigu.cloud.iotcloudspring.service.AiDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AiDeviceServiceImpl extends BaseServiceImpl<AiDeviceMapper, AiDevice> implements AiDeviceService {

    @Autowired
    @Qualifier("aiDeviceMapper")
    private AiDeviceMapper aiDeviceMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public AiDevice getDeviceByMacAddress(String macAddress) {
        if (StringUtils.isBlank(macAddress)) {
            return null;
        }
        QueryWrapper<AiDevice> wrapper = new QueryWrapper<>();
        wrapper.eq("mac_address", macAddress);
        return aiDeviceMapper.selectOne(wrapper);
    }

    private String getDeviceCacheKey(String deviceId) {
        String safeDeviceId = deviceId.replace(":", "_").toLowerCase();
        String dataKey = String.format("ota:activation:data:%s", safeDeviceId);
        return dataKey;
    }

    @Override
    public String geCodeByDeviceId(String deviceId) {
        String dataKey = getDeviceCacheKey(deviceId);

        Map<Object, Object> cacheMap = redisTemplate.opsForHash().entries(dataKey);
        if (cacheMap != null && cacheMap.containsKey("activation_code")) {
            String cachedCode = (String) cacheMap.get("activation_code");
            return cachedCode;
        }
        return null;
    }

    @Override
    public AiDevice getDeviceById(String deviceId) {
        LambdaQueryWrapper<AiDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiDevice::getId, deviceId);
        return aiDeviceMapper.selectOne(queryWrapper);
    }

    private DeviceReportRespDTO.ServerTime buildServerTime() {
        DeviceReportRespDTO.ServerTime serverTime = new DeviceReportRespDTO.ServerTime();
        TimeZone tz = TimeZone.getDefault();
        serverTime.setTimestamp(Instant.now().toEpochMilli());
        serverTime.setTimeZone(tz.getID());
        serverTime.setTimezone_offset(tz.getOffset(System.currentTimeMillis()) / (60 * 1000));
        return serverTime;
    }

    public DeviceReportRespDTO.Activation buildActivation(String deviceId, DeviceReportReqDTO deviceReport) {
        DeviceReportRespDTO.Activation code = new DeviceReportRespDTO.Activation();

        String cachedCode = geCodeByDeviceId(deviceId);

        if (StringUtils.isNotBlank(cachedCode)) {
            code.setCode(cachedCode);
//            code.setMessage(frontedUrl + "\n" + cachedCode);
        } else {
            String newCode = RandomUtil.randomNumbers(6);
            code.setCode(newCode);
//            code.setMessage(frontedUrl + "\n" + newCode);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", deviceId);
            dataMap.put("mac_address", deviceId);

            dataMap.put("board", (deviceReport.getChipModelName() != null) ? deviceReport.getChipModelName()
                    : (deviceReport.getBoard() != null ? deviceReport.getBoard().getType() : "unknown"));
            dataMap.put("app_version", (deviceReport.getApplication() != null)
                    ? deviceReport.getApplication().getVersion()
                    : null);

            dataMap.put("deviceId", deviceId);
            dataMap.put("activation_code", newCode);

            // 写入主数据 key
            String dataKey = getDeviceCacheKey(deviceId);
            redisTemplate.opsForHash().putAll(dataKey, dataMap);
            redisTemplate.expire(dataKey, 24, TimeUnit.HOURS);

            // 写入反查激活码 key
            String codeKey = "ota:activation:code:" + newCode;
            redisTemplate.opsForValue().set(codeKey, deviceId, 24, TimeUnit.HOURS);
        }
        return code;
    }

    @Override
    public DeviceReportRespDTO checkDeviceActive(String macAddress, String clientId,
                                                 DeviceReportReqDTO deviceReport) {
        DeviceReportRespDTO response = new DeviceReportRespDTO();
        response.setServer_time(buildServerTime());
        // todo: 此处是固件信息，目前是针对固件上传上来的版本号再返回回去
        // 在未来开发了固件更新功能，需要更换此处代码，
        // 或写定时任务定期请求虾哥的OTA，获取最新的版本讯息保存到服务内
        DeviceReportRespDTO.Firmware firmware = new DeviceReportRespDTO.Firmware();
        firmware.setVersion(deviceReport.getApplication().getVersion());
        firmware.setUrl("http://localhost:8002/xiaozhi/ota/download");
        response.setFirmware(firmware);

        AiDevice deviceById = getDeviceById(macAddress);
        if (deviceById != null) { // 如果设备存在，则更新上次连接时间
            deviceById.setLastConnectedAt(new Date());
            aiDeviceMapper.updateById(deviceById);
        } else { // 如果设备不存在，则生成激活码
            DeviceReportRespDTO.Activation code = buildActivation(macAddress, deviceReport);
            response.setActivation(code);
        }

        return response;
    }


}
