package com.cn.config;

import cn.hutool.http.Header;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ngcly
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                //指定包
                .apis(RequestHandlerSelectors.basePackage("com.cn.controller"))
                //所有路径
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(security())
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
                                        .description("系统异常").build()))
                .globalRequestParameters(globalRequestParameters());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("接口文档API")
                .description("音书API文档说明")
                .termsOfServiceUrl("https://ngcly.cn")
                .version("1.0")
                .build();
    }

    private List<SecurityScheme> security() {
        ApiKey apiKey = new ApiKey(Header.AUTHORIZATION.toString(), Header.AUTHORIZATION.toString(), ParameterType.HEADER.toString());
        return Collections.singletonList(apiKey);
    }

    private List<RequestParameter> globalRequestParameters() {
        RequestParameterBuilder parameterBuilder = new RequestParameterBuilder()
                .in(ParameterType.HEADER)
                .name(Header.AUTHORIZATION.toString())
                .required(false)
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)));
        return Collections.singletonList(parameterBuilder.build());
    }

}
