package com.dingding.mid.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Doctor4JavaEE
 * @since 2023/9/27
 * @ex ex配置
 */
@Component
@ConfigurationProperties("workflow")
@Data
public class FlowableExConfig {

    private String dbType;
    private String emailHost;
    private Integer emailPort;
    private String emailAddr;
    private String emailPassword;
}
