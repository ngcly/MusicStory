import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author ngcly
 */
class JasyptTest {
    @Test
    void encode(){
        System.setProperty("jasypt.encryptor.password","test_salt");
        StringEncryptor encryptor = new DefaultLazyEncryptor(new StandardEnvironment());
        String password = "password_test";
        String encryptStr = encryptor.encrypt(password);
        System.out.println(encryptStr);
        Assertions.assertEquals(password,encryptor.decrypt(encryptStr));
    }
}
