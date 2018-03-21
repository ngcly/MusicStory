package com.cn.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import groovy.util.logging.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 阿里Druid 数据连接池配置
 *
 * @author chen
 * @date 2018-01-02 17:30
 */
@Slf4j
@Configuration
@EnableTransactionManagement // 启注解事务管理
public class DruidConfig {
    @Bean
    public DataSource dataSource(Environment environment) {
        return DruidDataSourceBuilder
                .create()
                .build(environment, "spring.datasource.druid.");
    }
}
