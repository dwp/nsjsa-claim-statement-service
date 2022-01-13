package uk.gov.dwp.jsa.statement.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dwp.jsa.security.TokenProvider;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SecurityConfig.class})
@SpringBootTest(properties = "spring.profiles.active:not_local_test")
public class SecurityConfigTest {

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private Environment environment;

    @Autowired
    private SecurityConfig config;

    @Test
    public void test_SecurityConfigNotNull() {
        Assert.assertNotNull(config);
    }

}
