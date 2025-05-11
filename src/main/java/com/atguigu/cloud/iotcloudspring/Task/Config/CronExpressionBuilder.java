package com.atguigu.cloud.iotcloudspring.Task.Config;

import java.time.LocalTime;

public class CronExpressionBuilder {

    /**
     * 每天在指定时刻执行
     * @param time LocalTime，比如 08:30:00
     * @return Cron 表达式，如 "0 30 8 * * ?"
     */
    public static String dailyAt(LocalTime time) {
        // Cron 顺序：秒 分 时 日 月 周
        return String.format("%d %d %d * * ?",
                time.getSecond(),
                time.getMinute(),
                time.getHour());
    }

    /**
     * 直接传入你自己的 Cron 表达式则可跳过此 Builder
     */
}
