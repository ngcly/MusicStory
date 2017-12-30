package com.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
}