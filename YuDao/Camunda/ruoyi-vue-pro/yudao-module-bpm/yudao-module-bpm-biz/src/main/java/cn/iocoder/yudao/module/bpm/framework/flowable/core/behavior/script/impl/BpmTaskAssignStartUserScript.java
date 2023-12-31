package cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior.script.impl;

import cn.hutool.core.map.MapUtil;
import cn.iocoder.yudao.framework.common.util.collection.SetUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.bpm.constants.WorkFlowConstants;
import cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import cn.iocoder.yudao.module.bpm.service.task.BpmProcessInstanceService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * 分配给发起人审批的 Script 实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskAssignStartUserScript implements BpmTaskAssignScript {

    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        Map<String, Object> variables = execution.getVariables();
        Long startUserId = MapUtil.getLong(variables, WorkFlowConstants.PROCESS_INSTANCE_STARTER_USER_ID);
        return SetUtils.asSet(startUserId);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.START_USER;
    }

}
