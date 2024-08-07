import com.cn.util.IpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author chenning
 */
class IpUtilTest {

    @Test
    void test_getIpAddress() {
        String ipAddr = IpUtil.getIpAddresses("111.175.37.78");
        Assertions.assertNotNull(ipAddr);
    }

}
