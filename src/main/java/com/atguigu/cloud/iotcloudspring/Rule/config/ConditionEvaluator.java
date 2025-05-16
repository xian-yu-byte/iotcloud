package com.atguigu.cloud.iotcloudspring.Rule.config;

import com.atguigu.cloud.iotcloudspring.Rule.mapper.ConditionGroupMapper;
import com.atguigu.cloud.iotcloudspring.Rule.mapper.ConditionItemMapper;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.Rule;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleConditionGroup;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleConditionItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConditionEvaluator {

    private final ConditionGroupMapper groupMapper;
    private final ConditionItemMapper itemMapper;

    public boolean testBatch(Rule rule,
                             Map<Long, BigDecimal> values) {

        List<RuleConditionGroup> groups =
                groupMapper.selectByRuleId(rule.getId());

        for (RuleConditionGroup g : groups) {
            boolean groupOK = true;

            for (RuleConditionItem it :
                    itemMapper.selectByGroupId(g.getId())) {

                BigDecimal v = values.get(it.getDataId());
                if (v == null || !compare(it, v)) { // 缺数据或不满足
                    groupOK = false;
                    break;
                }
            }
            if (groupOK && "OR".equals(rule.getOuterLogicOp())) return true;
            if (!groupOK && !"OR".equals(rule.getOuterLogicOp())) return false;
        }
        return !"OR".equals(rule.getOuterLogicOp()); // 默认为 AND
    }

    private boolean compare(RuleConditionItem it, BigDecimal v) {
        return switch (it.getCompareOp()) {
            case ">" -> v.compareTo(it.getThresholdValue()) > 0;
            case ">=" -> v.compareTo(it.getThresholdValue()) >= 0;
            case "<" -> v.compareTo(it.getThresholdValue()) < 0;
            case "<=" -> v.compareTo(it.getThresholdValue()) <= 0;
            case "=" -> v.compareTo(it.getThresholdValue()) == 0;
            case "!=" -> v.compareTo(it.getThresholdValue()) != 0;
            case "BETWEEN" -> it.getThresholdLow() != null && it.getThresholdHigh() != null &&
                    v.compareTo(it.getThresholdLow()) >= 0 &&
                    v.compareTo(it.getThresholdHigh()) <= 0;
            default -> false;
        };
    }
}
