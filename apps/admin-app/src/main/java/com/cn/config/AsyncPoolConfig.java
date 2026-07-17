package com.cn.config;
 
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
import java.util.concurrent.Executor;
 
/**
 * 异步任务与 Web MVC 配置
 * 统一采用 JVM 虚拟线程 (Virtual Threads)，注册 Thymeleaf Layout 方言及全局 HTMX 局部适配拦截器
 *
 * @author ngcly
 */
@Slf4j
@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncPoolConfig implements AsyncConfigurer, WebMvcConfigurer {
    private final HtmxLayoutInterceptor htmxLayoutInterceptor;
 
    /**
     * 显式注册 Thymeleaf Layout Dialect Bean
     * 确保 Thymeleaf 能够正确识别与解析 layout:decorate 和 layout:fragment 布局装饰器
     */
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
 
    @Bean
    public SimpleAsyncTaskExecutor asyncExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("virtual-async#");
        executor.setVirtualThreads(true);
        return executor;
    }
 
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(asyncExecutor());
    }
 
    @Override
    public Executor getAsyncExecutor() {
        return asyncExecutor();
    }
 
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error(String.format("执行异步任务'%s'", method), ex);
    }
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(htmxLayoutInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**", "/captcha", "/login", "/logout");
    }
}
