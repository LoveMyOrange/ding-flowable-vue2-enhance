//package com.dingding.mid.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
//import com.baomidou.mybatisplus.core.MybatisConfiguration;
//import com.baomidou.mybatisplus.core.config.GlobalConfig;
//import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
//import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import com.dingding.mid.utils.SpringContextHolder;
//import com.github.pagehelper.PageInterceptor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.logging.slf4j.Slf4jImpl;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.type.JdbcType;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.util.*;
//
///**
// * MybatisPlus配置类
// */
//@Slf4j
//@Configuration
//@ComponentScan("com")
//@MapperScan(basePackages = {"com.dingding.mid.mapper"})
//public class MybatisPlusConfig {
//
//    /**
//     * 对接数据库的实体层
//     */
//    static final String ALIASES_PACKAGE = "com.dingding.mid.entity";
//
//
//    @Primary
//    @Bean(name = "dataSourceSystem")
//    public DataSource dataSourceOne() throws Exception{
//        return druidDataSource();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    protected DataSource druidDataSource() throws Exception{
//        return DruidDataSourceBuilder.create().build();
//    }
//
//
//
//
//
//    @Bean(name = "sqlSessionFactorySystem")
//    public SqlSessionFactory sqlSessionFactoryOne(@Qualifier("dataSourceSystem") DataSource dataSource) throws Exception {
//        return createSqlSessionFactory(dataSource);
//    }
//
//
//    public Resource[] resolveMapperLocations() {
//        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//        List<String> mapperLocations = new ArrayList<>();
//        mapperLocations.add("classpath:mapper/*/*.xml");
//        mapperLocations.add("classpath:mapper/*/*/*.xml");
//        List<Resource> resources = new ArrayList<Resource>();
//        for (String mapperLocation : mapperLocations) {
//            try {
//                Resource[] mappers = resourceResolver.getResources(mapperLocation);
//                resources.addAll(Arrays.asList(mappers));
//            } catch (IOException e) {
//                // ignore
//            }
//        }
//        return resources.toArray(new Resource[0]);
//    }
//
//    public SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
//        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setVfs(SpringBootVFS.class);
//        bean.setTypeAliasesPackage(ALIASES_PACKAGE);
//        bean.setMapperLocations(resolveMapperLocations());
//        bean.setConfiguration(configuration(dataSource));
//        bean.setPlugins(new Interceptor[]{pageHelper()});
//        return bean.getObject();
//    }
//
//
//    public PageInterceptor pageHelper() {
//        PageInterceptor pageHelper = new PageInterceptor();
//        // 配置PageHelper参数
//        Properties properties = new Properties();
//        properties.setProperty("dialectAlias", "kingbasees8=com.github.pagehelper.dialect.helper.MySqlDialect");
//        properties.setProperty("autoRuntimeDialect", "true");
//        properties.setProperty("offsetAsPageNum", "false");
//        properties.setProperty("rowBoundsWithCount", "false");
//        properties.setProperty("pageSizeZero", "true");
//        properties.setProperty("reasonable", "false");
//        properties.setProperty("supportMethodsArguments", "false");
//        properties.setProperty("returnPageInfo", "none");
//        pageHelper.setProperties(properties);
//        return pageHelper;
//    }
//
//    public MybatisConfiguration configuration(DataSource dataSource){
//        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
//        mybatisConfiguration.setMapUnderscoreToCamelCase(true);
//        mybatisConfiguration.setCacheEnabled(true);
//        mybatisConfiguration.setLogImpl(Slf4jImpl.class);
//        mybatisConfiguration.setJdbcTypeForNull(JdbcType.NULL);
//
//        return mybatisConfiguration;
//    }
//    @Bean
//    public IKeyGenerator keyGenerator() {
//        return new H2KeyGenerator();
//    }
//
//
//
//
//}
