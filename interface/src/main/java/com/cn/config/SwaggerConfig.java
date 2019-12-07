package com.cn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.security.Principal;
import java.util.Arrays;

/**
 * @author ngcly
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指定包
                .apis(RequestHandlerSelectors.basePackage("com.cn.controller"))
                //所有路径
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Authentication.class, Principal.class)
                .globalResponseMessage(RequestMethod.GET,
                        Arrays.asList(new ResponseMessageBuilder()
                                        .code(0)
                                        .message("请求成功")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(500)
                                        .message("系统异常").build()))
                .globalResponseMessage(RequestMethod.POST,
                        Arrays.asList(new ResponseMessageBuilder()
                                        .code(0)
                                        .message("请求成功")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("参数不合法").build(),
                                new ResponseMessageBuilder()
                                        .code(500)
                                        .message("系统异常").build()))
                .globalResponseMessage(RequestMethod.PUT,
                        Arrays.asList(new ResponseMessageBuilder()
                                        .code(0)
                                        .message("执行成功")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(500)
                                        .message("系统异常").build()))
                .globalResponseMessage(RequestMethod.DELETE,
                        Arrays.asList(new ResponseMessageBuilder()
                                        .code(0)
                                        .message("请求成功")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(500)
                                        .message("系统异常").build()));
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("接口文档API")
                .description("音书API文档说明")
                .termsOfServiceUrl("http://ngcly.cn")
                .version("1.0")
                .build();
    }
}
