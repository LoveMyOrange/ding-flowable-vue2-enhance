package com.dingding.mid.listener;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;


/**
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component
public class ServiceListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
    }
}
