package cn.iocoder.yudao.module.bpm.framework.flowable.config;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.module.bpm.framework.flowable.core.listener.GlobalProcessListener;
import cn.iocoder.yudao.module.bpm.service.definition.BpmTaskAssignRuleService;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BPM 模块的 Flowable 配置类
 *
 * @author jason
 */
@Configuration(proxyBeanMethods = false)
public class BpmFlowableConfiguration {
    @Resource
    private DataSource dataSource;
    @Resource
    private PlatformTransactionManager transactionManager;
//    @Resource
//    private IdWorkerIdGenerator idWorkerIdGenerator;
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

            Map<Object,Object> params= new HashMap<>();
            BpmTaskAssignRuleService bpmTaskAssignRuleService = SpringUtil.getBean(BpmTaskAssignRuleService.class);
            params.put("bpmTaskAssignRuleService",bpmTaskAssignRuleService);
            processEngineConfiguration.setBeans(params);
        }
    }
//    @Bean
//    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
//            ) {
//        SpringProcessEngineConfiguration springProcessEngineConfiguration= new SpringProcessEngineConfiguration();
//        Map<Object,Object> params= new HashMap<>();
//        BpmTaskAssignRuleService bpmTaskAssignRuleService = SpringUtil.getBean(BpmTaskAssignRuleService.class);
//        params.put("bpmTaskAssignRuleService",bpmTaskAssignRuleService);
//        springProcessEngineConfiguration.setBeans(params);
//        springProcessEngineConfiguration.setDataSource(dataSource);
//        springProcessEngineConfiguration.setTransactionManager(transactionManager);
//        System.err.println("触发Camunda初始化");
//        return springProcessEngineConfiguration;
//    }


}
