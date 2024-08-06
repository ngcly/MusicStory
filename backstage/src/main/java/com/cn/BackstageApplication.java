package com.cn;

import com.cn.enums.ConfigEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 后台主启动类
 * <p>
 * springboot 启动类不能直接放在 java 文件下 不然会报错
 * springboot 扫描包默认是以启动类所在的包 从上往下扫描
 * 要解决这个可以使用：basePackageClasses={},basePackages={}
 * &#064;ComponentScan(basePackages={"com.cn"})
 *
 * @author ngcly
 * @since 2017-12-30 15:51
 */
@EnableCaching
@SpringBootApplication
public class BackstageApplication {
    public static void main(String[] args) {
        ConfigEnum configEnum = ConfigEnum.JASYPT_ENCRYPTOR;
        System.setProperty(configEnum.getKey(),configEnum.getValue());
        SpringApplication.run(BackstageApplication.class, args);
    }

}