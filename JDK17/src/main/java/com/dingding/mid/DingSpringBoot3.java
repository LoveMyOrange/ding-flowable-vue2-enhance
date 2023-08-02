package com.dingding.mid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


/**
 */
@SpringBootApplication(scanBasePackages = "com")
@ComponentScan("com")
@MapperScan(basePackages = {"com.dingding.mid.mapper"})
public class DingSpringBoot3 extends SpringBootServletInitializer implements
    CommandLineRunner {

    public static void main(String[] args)  {
        SpringApplication springApplication = new SpringApplication(DingSpringBoot3.class);
        springApplication.run(args);
        System.out.println("DingDing启动完成");
    }


    @Override
    public void run(String... args) throws Exception {

    }



}
