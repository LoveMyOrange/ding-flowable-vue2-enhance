package com.dingding.mid;

import java.io.*;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.dingding.mid.utils.MinioUploadUtil;
import com.dingding.mid.utils.SpringContextHolder;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.Cleanup;
import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.javax.el.ExpressionFactory;
import org.flowable.common.engine.impl.javax.el.ValueExpression;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 */
@SpringBootApplication(scanBasePackages = "com")
public class DingDingAdminApplication extends SpringBootServletInitializer implements
    CommandLineRunner {

    public static void main(String[] args)  {
        SpringApplication springApplication = new SpringApplication(DingDingAdminApplication.class);
        springApplication.run(args);
        System.out.println("DingDing启动完成");
    }


    @Override
    public void run(String... args) throws Exception {

    }



}
