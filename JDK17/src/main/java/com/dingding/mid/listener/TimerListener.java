package com.dingding.mid.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingding.mid.dto.json.ChildNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;


/**
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component
@Slf4j
public class TimerListener implements ExecutionListener {

    /**
     * 自行编写 触发逻辑既可
     * @param execution
     */
    @Override
    public void notify(DelegateExecution execution) {
        log.info("========");
        System.err.println("exection触发了");
    }
}
