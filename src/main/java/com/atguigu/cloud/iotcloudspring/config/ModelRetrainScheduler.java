package com.atguigu.cloud.iotcloudspring.config;


import com.atguigu.cloud.iotcloudspring.DTO.Device.DataPointDTO;
import com.atguigu.cloud.iotcloudspring.DTO.TrainingLog;
import com.atguigu.cloud.iotcloudspring.mapper.DeviceMapper;
import com.atguigu.cloud.iotcloudspring.mapper.TrainingLogMapper;
import com.atguigu.cloud.iotcloudspring.service.AnomalyService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ModelRetrainScheduler {

    private static final int MIN_NEW_RECORDS = 100;          // 阈值：最少新增条数
    private static final long RETRAIN_INTERVAL_HOURS = 3;    // 每隔 3 小时可训练一次

    private final DeviceMapper deviceMapper;
    private final TrainingLogMapper logMapper;
    private final AnomalyService anomalyService;

    /** 每 3 小时执行一次 */
    @Scheduled(cron = "0 0 */3 * * *")
    public void scheduleRetrain() {
        // 1. 拿到所有项目 ID
        List<Long> projectIds = logMapper.selectAllProjectIds();
        if (CollectionUtils.isEmpty(projectIds)) {
            return;
        }

        for (Long pid : projectIds) {
            TrainingLog log = logMapper.selectByProjectId(pid);
            LocalDateTime lastTime = log.getLastTrainedAt();
            // 2. 时间门槛
            if (lastTime.plusHours(RETRAIN_INTERVAL_HOURS).isAfter(LocalDateTime.now())) {
                continue;
            }
            // 3. 数据量门槛
            int newCount = deviceMapper.countSince(pid, lastTime);
            if (newCount < MIN_NEW_RECORDS) {
                continue;
            }
            // 4. 拉取用于训练的数据
            List<DataPointDTO> batch = deviceMapper.selectForTraining(pid);
            if (batch.size() < MIN_NEW_RECORDS) {
                continue;
            }
            // 5. 调用 Python 服务重训
            anomalyService.train(pid, batch);
            // 6. 更新日志表
            log.setLastTrainedAt(LocalDateTime.now());
            log.setLastRecordCount(deviceMapper.countSince(pid, LocalDateTime.MIN));
            logMapper.update(log);
        }
    }
}
