package com.cn;

import com.cn.enums.ConfigEnum;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试类
 *
 * @author chen
 * @date 2018-03-15 12:46
 */
@SpringBootTest
public class BackstageApplicationTests {
    @Autowired
    private StringEncryptor stringEncryptor;

    @BeforeAll
    public static void setupClass() {
        ConfigEnum configEnum = ConfigEnum.JASYPT_ENCRYPTOR;
        System.setProperty(configEnum.getKey(),configEnum.getValue());
    }


    @Test
    public void contextLoads() {
        System.out.println("Key: "+stringEncryptor.decrypt("BFmGqMfhERt8BVG81BuQFAI4L165TxyjahwvlTGx9QnjkKAWytNvhhtfiajw1mhsGTLh5JErMOFrjltogIkOUg=="));
    }
}
