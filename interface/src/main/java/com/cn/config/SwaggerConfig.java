package com.cn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.security.Principal;
import java.util.Arrays;

/**
 * @author ngcly
 */
@Configuration
@EnableOpenApi
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
                .globalResponses(HttpMethod.GET,
                        Arrays.asList(new ResponseBuilder()
                                        .code("0")
                                        .description("请求成功")
                                        .build(),
                                new ResponseBuilder()
                                        .code("500")
                                        .description("系统异常").build()))
                .globalResponses(HttpMethod.POST,
                        Arrays.asList(new ResponseBuilder()
                                        .code("0")
                                        .description("请求成功")
                                        .build(),
                                new ResponseBuilder()
                                        .code("400")
                                        .description("参数不合法").build(),
                                new ResponseBuilder()
                                        .code("500")
                                        .description("系统异常").build()))
                .globalResponses(HttpMethod.PUT,
                        Arrays.asList(new ResponseBuilder()
                                        .code("0")
                                        .description("执行成功")
                                        .build(),
                                new ResponseBuilder()
                                        .code("500")
                                        .description("系统异常").build()))
                .globalResponses(HttpMethod.DELETE,
                        Arrays.asList(new ResponseBuilder()
                                        .code("0")
                                        .description("请求成功")
                                        .build(),
                                new ResponseBuilder()
                                        .code("500")
                                        .description("系统异常").build()));
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
