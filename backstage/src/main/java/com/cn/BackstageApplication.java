package com.cn;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * 后台主启动类
 *
 * @author chen
 * @date 2017-12-30 15:51
 */
//springboot 启动类不能直接放在 java 文件下 不然会报错
//springboot 扫描包默认是以启动类所在的包 从上往下扫描
//要解决这个可以使用：basePackageClasses={},basePackages={}
//@ComponentScan(basePackages={"com.cn"})
@SpringBootApplication
public class BackstageApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackstageApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    private Connector createHTTPConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        //同时启用http（80）、https（443）两个端口
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(80);
        connector.setRedirectPort(443);
        return connector;
    }

}