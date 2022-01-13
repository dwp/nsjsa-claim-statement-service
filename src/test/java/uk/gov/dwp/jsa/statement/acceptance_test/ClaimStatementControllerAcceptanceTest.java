package uk.gov.dwp.jsa.statement.acceptance_test;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.dwp.jsa.security.WithMockUser;
import uk.gov.dwp.jsa.security.roles.Role;
import uk.gov.dwp.jsa.statement.Application;
import uk.gov.dwp.jsa.statement.acceptance_test.wiremock.WiremockUtils;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class, TestConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"local_test", "nosecure"})
public class ClaimStatementControllerAcceptanceTest {
    public static final String CLAIM_STATEMENT_URL = "/nsjsa/v1.1/claim-statement/claim/0f14d0ab-9605-4a62-a9e4-5ed26688389b";

    @Autowired
    private MockMvc mockMvc;


    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(WiremockUtils.WIREMOCK_PORT);

    @Rule
    public WireMockClassRule wiremockServer = wireMockRule;

    private WiremockUtils wiremockUtils;

    @Before
    public void setup() {
        wiremockUtils = new WiremockUtils(wireMockRule);
        wiremockUtils.givenAResponseForCircumstances();
        wiremockUtils.givenAResponseForBankDetails();
        wiremockUtils.givenAResponseForClaimant();
    }

    @Test
    @WithMockUser(role = Role.CCA)
    public void GetClaimStatement_ReturnsSummaryPageForThePrototype() throws Exception {
        Thread.sleep(2000);
        mockMvc.perform(get(CLAIM_STATEMENT_URL).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("I have read and understood the statement and enclosed notes")));
    }

    public void GetClaimStatement_Returns404() throws Exception {
        wiremockUtils.givenA404ForBankDetails();
        mockMvc.perform(get(CLAIM_STATEMENT_URL).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("I have read and understood the statement and enclosed notes")));
    }

}
