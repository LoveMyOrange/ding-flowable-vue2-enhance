package com.dingding.mid.config;

import com.dingding.mid.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LoveMyOrange
 * @create 2021-09-21 14:59
 */
@Configuration
public class IdWorkerConfig {

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
