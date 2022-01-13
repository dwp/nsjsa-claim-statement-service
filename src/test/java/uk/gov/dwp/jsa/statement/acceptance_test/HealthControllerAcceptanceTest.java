package uk.gov.dwp.jsa.statement.acceptance_test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.dwp.jsa.security.WithMockUser;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.dwp.jsa.security.roles.Role.CCA;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HealthControllerAcceptanceTest {

    public static final String HEALTH_PING = "/nsjsa/health/ping";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(role = CCA)
    public void GetHealthPing_ReturnsPong() throws Exception {
        mockMvc.perform(get(HEALTH_PING).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("pong")));
    }
}
