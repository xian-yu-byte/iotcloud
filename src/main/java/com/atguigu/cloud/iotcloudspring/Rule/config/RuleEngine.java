package com.atguigu.cloud.iotcloudspring.Rule.config;

import com.atguigu.cloud.iotcloudspring.Rule.mapper.*;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.*;
import com.atguigu.cloud.iotcloudspring.Rule.reault.ActionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class RuleEngine {

    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private RuleActionMapper actionMapper;
    @Resource
    private ConditionEvaluator condEvaluator;
    @Resource
    private ActionExecutor actionExecutor;
    @Resource
    private RuleLogMapper ruleLogMapper;

    private final ObjectMapper mapper;


    /**
     * 评估设备数据，满足则调用云函数
     */
    public void evaluate(Long deviceId, Long dataId, BigDecimal value) {
        Map<Long, BigDecimal> m = Map.of(dataId, value);
        evaluateBatch(deviceId, m);
    }

    /**
     * 批量版本：一次判所有条件
     */
    public void evaluateBatch(Long deviceId,
                              Map<Long, BigDecimal> values) {
        var lastEntry = values.entrySet().stream().reduce((a, b) -> b).get();
        Long dataId = lastEntry.getKey();
        BigDecimal value = lastEntry.getValue();
        LocalDateTime now = LocalDateTime.now();

        List<Rule> rules = ruleMapper.selectEnabledByDevice(deviceId);
        for (Rule r : rules) {
            if (!condEvaluator.testBatch(r, values)) continue;

            // 1. 先拉出该规则的所有动作
            List<RuleAction> actions = actionMapper.selectByRuleId(r.getId());

            // 2. 执行动作，得到结果
            ActionResult result = actionExecutor.runCloudFunctions(
                    r, actions,
                    deviceId,
                    mapper.valueToTree(values),
                    dataId,
                    value);

            // 3. 决定日志状态
            String status;
            if (result.isAllSuccess()) {
                status = "成功";
            } else if (result.getErrorMessages().size() < actions.size()) {
                status = "部分成功";
            } else {
                status = "失败";
            }

            // 4. 构造并写入日志
            RuleLog log = new RuleLog();
            log.setRuleId(r.getId());
            log.setDeviceId(deviceId);
            log.setDataId(dataId);
            log.setValue(value);
            log.setTriggerTime(now);
            log.setActionStatus(status);
            log.setMessage(String.join("; ", result.getErrorMessages()));
            ruleLogMapper.insert(log);
        }
    }
}