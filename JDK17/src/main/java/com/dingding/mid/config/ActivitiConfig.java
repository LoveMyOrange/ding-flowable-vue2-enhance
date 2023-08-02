/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 */

package com.dingding.mid.config;

import com.dingding.mid.job.IdWorkerIdGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import com.dingding.mid.job.CustomJobHandler;
import jakarta.annotation.Resource;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.cfg.HttpClientConfig;
import org.flowable.engine.impl.cfg.DelegateExpressionFieldInjectionMode;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.job.service.JobHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.variable.api.types.VariableType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author
 * @date
 * @desc
 */
@Configuration
public class ActivitiConfig {
	@Resource
	private  DataSource dataSource;
	@Resource
	private  PlatformTransactionManager transactionManager;
	@Resource
	private IdWorkerIdGenerator idWorkerIdGenerator;
	@Bean
	public SpringProcessEngineConfiguration getSpringProcessEngineConfiguration() {
		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		config.setActivityFontName("宋体");
		config.setAnnotationFontName("宋体");
		config.setLabelFontName("黑体");
		config.setDataSource(dataSource);
		config.setTransactionManager(transactionManager);
		config.setDisableIdmEngine(true);
		config.setDatabaseType(ProcessEngineConfigurationImpl.DATABASE_TYPE_MYSQL);
		config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		config.setDelegateExpressionFieldInjectionMode(DelegateExpressionFieldInjectionMode.MIXED);

		config.setIdGenerator(idWorkerIdGenerator);
		config.setAsyncExecutorActivate(Boolean.TRUE);
		HttpClientConfig httpClientConfig=new HttpClientConfig();
		//连接超时
		httpClientConfig.setConnectTimeout(1000000);
		//连接请求超时
		httpClientConfig.setConnectionRequestTimeout(1000000);
		//双端建立socket连接
		httpClientConfig.setSocketTimeout(1000000);
		//请求失败之后重试次数
		httpClientConfig.setRequestRetryLimit(3);
		config.setHttpClientConfig(httpClientConfig);
		config.setKnowledgeBaseCacheLimit(200);
		config.setProcessDefinitionCacheLimit(200);
		List<JobHandler> customJobHandlers =new ArrayList<>();
		customJobHandlers.add(new CustomJobHandler());
		config.setCustomJobHandlers(customJobHandlers);
		return config;
	}

	@Bean
	@Primary
	public TaskExecutor primaryTaskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

}
