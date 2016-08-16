package urim;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:application-test.properties")
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BaseIT {
    @Value("${server.port}")
    protected int port;

    protected URL base;
    protected RestTemplate template;

    @Test
    public void test() throws Exception {
    }
}