package com.cn;

import com.cn.enums.ConfigEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ngcly
 */
@SpringBootApplication
public class InterfaceApplication {
    public static void main(String[] args) {
        ConfigEnum configEnum = ConfigEnum.JASYPT_ENCRYPTOR;
        System.setProperty(configEnum.getKey(),configEnum.getValue());
        SpringApplication.run(InterfaceApplication.class, args);
    }
}
