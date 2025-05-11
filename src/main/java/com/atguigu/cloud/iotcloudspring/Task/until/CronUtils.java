package com.atguigu.cloud.iotcloudspring.Task.until;

import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CronUtils {

    public static LocalDateTime nextExecution(String cron) {
        CronExpression exp = CronExpression.parse(cron);
        ZonedDateTime next = exp.next(ZonedDateTime.now());
        if (next != null) {
            return next.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }
}
