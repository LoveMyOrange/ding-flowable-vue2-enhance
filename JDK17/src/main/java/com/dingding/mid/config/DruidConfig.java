//package com.dingding.mid.config;
//
//
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DruidConfig {
//
//    /**
//     * 配置Druid的监控
//     * 配置一个管理后台的Servlet
//     * @return
//     */
//    @Bean
//    public ServletRegistrationBean statViewServlet(){
//        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        Map<String, String> initParams = new HashMap<>(4);
//        //配置druid页面监控的账户密码
//        initParams.put("loginUsername","dingding");
//        initParams.put("loginPassword","123456");
//        //默认就是允许所有访问
//        initParams.put("allow","");
//        //黑名单  注意 黑名单优先于 allow白名单加载 两个名单中不可重复
//        initParams.put("deny","192.168.10.21");
//
//        bean.setInitParameters(initParams);
//        return bean;
//    }
//
//
//    /**
//     * 配置一个web监控的filter
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean webStatFilter(){
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new WebStatFilter());
//
//        Map<String, String> initParams = new HashMap<>(16);
//        initParams.put("exclusions","*.js,*.css,/druid/*");
//
//        bean.setInitParameters(initParams);
//
//        bean.setUrlPatterns(Arrays.asList("/*"));
//
//        return  bean;
//    }
//}
