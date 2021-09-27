package com.cn;

import com.cn.enums.ConfigEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ngcly
 */
@EnableOpenApi
@SpringBootApplication
public class InterfaceApplication {
    public static void main(String[] args) {
        ConfigEnum configEnum = ConfigEnum.JASYPT_ENCRYPTOR;
        System.setProperty(configEnum.getKey(),configEnum.getValue());
        SpringApplication.run(InterfaceApplication.class, args);
    }
}
