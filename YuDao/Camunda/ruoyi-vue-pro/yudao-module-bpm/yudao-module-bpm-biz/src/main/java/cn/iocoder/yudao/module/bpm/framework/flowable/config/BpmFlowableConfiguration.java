package cn.iocoder.yudao.module.bpm.framework.flowable.config;

import cn.hutool.core.collection.ListUtil;
import cn.iocoder.yudao.module.bpm.framework.flowable.core.listener.GlobalProcessListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * BPM 模块的 Flowable 配置类
 *
 * @author jason
 */
@Configuration(proxyBeanMethods = false)
public class BpmFlowableConfiguration {

    /**
     * BPM 模块的 ProcessEngineConfigurationConfigurer 实现类：
     *
     * 1. 设置各种监听器
     * 2. 设置自定义的 ActivityBehaviorFactory 实现
     */
//    @Bean
//    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> bpmProcessEngineConfigurationConfigurer(
//            ObjectProvider<FlowableEventListener> listeners,
//            BpmActivityBehaviorFactory bpmActivityBehaviorFactory) {
//        return configuration -> {
//            // 注册监听器，例如说 BpmActivityEventListener
//            configuration.setEventListeners(ListUtil.toList(listeners.iterator()));
//            // 设置 ActivityBehaviorFactory 实现类，用于流程任务的审核人的自定义
//            configuration.setActivityBehaviorFactory(bpmActivityBehaviorFactory);
//        };
//    }

    @Component
    public class CamundaGlobalListenerPlugin extends AbstractProcessEnginePlugin {
        @Override
        public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
            List<BpmnParseListener> preParseListeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
            if(preParseListeners == null) {
                preParseListeners = new ArrayList<BpmnParseListener>();
                processEngineConfiguration.setCustomPreBPMNParseListeners(preParseListeners);
            }
            preParseListeners.add(new GlobalProcessListener());
        }
    }

    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        System.err.println("触发Camunda初始化");
        return springProcessEngineConfiguration;
    }


}
