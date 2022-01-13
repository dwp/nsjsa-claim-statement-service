package uk.gov.dwp.jsa.statement.acceptance_test;


import com.amazonaws.util.IOUtils;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.dwp.jsa.security.WithMockUser;
import uk.gov.dwp.jsa.security.roles.Role;
import uk.gov.dwp.jsa.statement.Application;
import uk.gov.dwp.jsa.statement.acceptance_test.wiremock.WiremockUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
//Starts a proper TOMCAT server
@ContextConfiguration(classes = {Application.class, TestConfig.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active:local_test,nosecure")
@AutoConfigureMockMvc
public class CreateStatementAcceptanceTest {

    public static final String FORM_CREATE_STATEMENT_URL = "/nsjsa/v1.1/claim-statement/claim/0f14d0ab-9605-4a62-a9e4-5ed26688389b";

    @Autowired
    private TestRestTemplate webClient;

    private WiremockUtils wiremockUtils;

    @Autowired
    private MockMvc mockMvc;


    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(WiremockUtils.WIREMOCK_PORT);

    @Rule
    public WireMockClassRule wiremockServer = wireMockRule;

    @Before
    public void setup(){
        wiremockUtils  = new WiremockUtils(wireMockRule);
        wiremockUtils.givenAResponseForBankDetails();
        wiremockUtils.givenAResponseForClaimant();
    }



    @Test
    @WithMockUser(role = Role.CCA)
    public void createStatementTestAcceptance() throws Exception {
        wiremockUtils.givenAResponseForCircumstances();
        String rightSourceCode = getFileWithNoSpaces("summary_template_test.txt")
                .replaceAll("\\s", "");
        MvcResult body = mockMvc.perform(get(FORM_CREATE_STATEMENT_URL)
                .with(csrf())).andReturn();

        String formatedBody = body.getResponse().getContentAsString().replaceAll("\\s","");
        assertThat(formatedBody).isEqualTo(rightSourceCode);
    }

    @Test
    @WithMockUser(role = Role.CCA)
    public void givenWelshLocaleInCircumstances_WhenWeRequestTheClaimToPrint_ThenItisInWelsh() throws Exception {
        wiremockUtils.givenAResponseForCircumstancesInWelsh();
        String rightSourceCode = getFileWithNoSpaces("summary_template_test_cy.txt");
        MvcResult body = mockMvc.perform(get(FORM_CREATE_STATEMENT_URL)
                .with(csrf())).andReturn();

        String formatedBody = body.getResponse().getContentAsString().replaceAll("\\s","");
        assertThat(formatedBody).isEqualTo(rightSourceCode);
    }

    private String getFileWithNoSpaces(String fileName) throws IOException {
       return getFileWithUtil(fileName).replaceAll("\\s","");
    }

    private String getFileWithUtil(String fileName) throws IOException {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        return result;
    }
}
