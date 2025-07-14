package com.atguigu.cloud.iotcloudspring.Rule.config;

import com.atguigu.cloud.iotcloudspring.Rule.pojo.Rule;
import com.atguigu.cloud.iotcloudspring.Rule.pojo.RuleAction;
import com.atguigu.cloud.iotcloudspring.Rule.reault.ActionResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ActionExecutor {

    @Resource
    private FunctionExecutor functionExecutor;

    private final ObjectMapper mapper;

    /**
     * 执行一条规则里的所有 CLOUD_FUNCTION 动作
     *
     * @param rule      规则主表实体
     * @param actions   这条规则的动作列表
     * @param deviceId  触发的设备 ID
     * @param valueNode 触发值 (JsonNode)
     */
    public ActionResult runCloudFunctions(Rule rule,
                                          List<RuleAction> actions,
                                          Long deviceId,
                                          JsonNode valueNode,
                                          Long triggerDataId,
                                          BigDecimal triggerValue) {
        boolean allSuccess = true;
        List<String> errs = new ArrayList<>();
        log.info("evaluate rule={}, actions={}", rule.getId(), actions.size());

        for (RuleAction act : actions) {
            if (!("CLOUD_FUNCTION".equals(act.getActionType())
                    || "云函数编辑".equals(act.getActionType())
            ||"向设备下发命令".equals(act.getActionType())))
                continue;

            try {
                JsonNode ap = mapper.readTree(act.getActionParams());

                ObjectNode ctx = mapper.createObjectNode();
                ctx.put("ruleId", rule.getId());
                ctx.put("deviceId", deviceId);

                ctx.set("value", valueNode);                // 全量 map
                ctx.put("triggerDataId", triggerDataId);            // 单值
                ctx.put("triggerValue", triggerValue.doubleValue());

                ctx.put("topic", ap.path("topic").asText());
                ctx.set("payload", ap.path("payload"));

                functionExecutor.execute(ap.path("functionId").asLong(), ctx);

            } catch (Exception e) {
                allSuccess = false;
                errs.add("actionId=" + act.getId() + "：" + e.getMessage());
            }
        }
        return new ActionResult(allSuccess, errs);
    }
}
