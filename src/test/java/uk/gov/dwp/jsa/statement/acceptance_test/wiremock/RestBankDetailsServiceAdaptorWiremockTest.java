package uk.gov.dwp.jsa.statement.acceptance_test.wiremock;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.jsa.adaptors.RestfulBankDetailsServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local_test")
public class RestBankDetailsServiceAdaptorWiremockTest {

    private static final UUID NINO = UUID.randomUUID();

    private static final String ACCOUNT_NUMBER = "01234567";

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(WiremockUtils.WIREMOCK_PORT);

    @Rule
    public WireMockClassRule wiremockServer = wireMockRule;

    // A service that calls out over HTTP to localhost:${wiremock.port}
    @Autowired
    private RestfulBankDetailsServiceAdaptor serviceToTest;


    private CompletableFuture<Optional<BankDetails>> serviceResponse;

    private String bankDetailsResourceUrl
            = "http://localhost:" + WiremockUtils.WIREMOCK_PORT + "/nsjsa/v1/claim/QA12312312312/bank-details";

    private RestTemplate webClient;

    private ResponseEntity<String> bankDetailsRawresponse;


    @Before
    public void setup() {
        webClient = new RestTemplate();
        try {
            // TODO: Remove - Suggested workaround from wiremock
            // https://github.com/tomakehurst/wiremock/issues/97
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void givenTheServiceBankDetails_WeRequestUsingNino_ThenReceive200AndData() throws ExecutionException, InterruptedException {
        givenAResponse();
        whenWeCallTheService();
        thenValuesAreTheSame();

    }

    private void givenAResponse() {
        WiremockUtils wiremockUtils = new WiremockUtils(wireMockRule);
        wiremockUtils.givenAResponseForBankDetails();
    }

    private void whenWeCallTheService() {
        serviceResponse = this.serviceToTest.getBankDetailsByClaimantId(NINO);
    }

    private void thenValuesAreTheSame() throws ExecutionException, InterruptedException {
        Optional<BankDetails> bankDetailsOptional = serviceResponse.get();
        assertNotNull("should not be null", bankDetailsOptional);
        assertTrue("Should be present", bankDetailsOptional.isPresent());
        BankDetails bankDetails = bankDetailsOptional.get();
        assertNotNull("should not be null", bankDetails);
        Assertions.assertThat(bankDetails.getAccountNumber()).isEqualTo(ACCOUNT_NUMBER);
    }


}
