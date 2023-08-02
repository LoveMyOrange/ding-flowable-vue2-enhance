package com.dingding.mid.job;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.job.service.JobHandler;
import org.flowable.job.service.impl.persistence.entity.JobEntity;
import org.flowable.variable.api.delegate.VariableScope;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 21:42
 */
@Slf4j
public class CustomJobHandler  implements JobHandler {
    public static final String TYPE = "custom-handler";
    @Override
    public String getType() {
        return "custom-handler";
    }

    @Override
    public void execute(JobEntity jobEntity, String s, VariableScope variableScope, CommandContext commandContext) {
        log.info("============执行自定义定时任务============");
        log.info("定时任务详情={}", JSON.toJSONString(jobEntity));
    }
}
