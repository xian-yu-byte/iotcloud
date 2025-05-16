package com.atguigu.cloud.iotcloudspring.Rule.config;

import com.atguigu.cloud.iotcloudspring.Rule.mapper.RuleActionMapper;
import com.atguigu.cloud.iotcloudspring.Rule.mapper.RuleMapper;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.Rule;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleAction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RuleEngine {

    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private RuleActionMapper actionMapper;
    @Resource
    private ConditionEvaluator condEvaluator;   // 见后面
    @Resource
    private ActionExecutor actionExecutor;

    private final ObjectMapper mapper;

    public RuleEngine(ObjectMapper mapper) {
        this.mapper = mapper;
    }

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

        /* 选最后写入的那一条作为 trigger，也可按需改 */
        Map.Entry<Long, BigDecimal> last =
                values.entrySet().stream().reduce((a, b) -> b).orElse(null);

        JsonNode valueJson = mapper.valueToTree(values);
        List<Rule> rules = ruleMapper.selectEnabledByDevice(deviceId);

        for (Rule r : rules) {
            if (condEvaluator.testBatch(r, values)) {
                List<RuleAction> acts = actionMapper.selectByRuleId(r.getId());

                actionExecutor.runCloudFunctions(
                        r, acts, deviceId,
                        valueJson,
                        last.getKey(),          // triggerDataId
                        last.getValue());       // triggerValue
            }
        }
    }
}