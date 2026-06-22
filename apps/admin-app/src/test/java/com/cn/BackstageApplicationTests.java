package com.cn;

import com.cn.dao.BookRepository;
import com.cn.enums.ConfigEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * 测试类
 *
 * @author ngcly
 * @date 2018-03-15 12:46
 */
@SpringBootTest
@ActiveProfiles("test")
class BackstageApplicationTests {

    @MockitoBean
    private BookRepository bookRepository;

    @BeforeAll
    static void setup(){
        ConfigEnum configEnum = ConfigEnum.JASYPT_ENCRYPTOR;
        System.setProperty(configEnum.getKey(),configEnum.getValue());
    }

    @Test
    void contextLoads() {

    }
}
