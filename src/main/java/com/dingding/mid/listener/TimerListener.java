package com.dingding.mid.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingding.mid.dto.json.ChildNode;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component
public class TimerListener implements ExecutionListener {
    @Resource
    private RepositoryService repositoryService;
    @Override
    public void notify(DelegateExecution execution) {
    }
}
