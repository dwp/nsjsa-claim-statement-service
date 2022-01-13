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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dwp.jsa.adaptors.CircumstancesServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local_test")
public class RestCircumstancesServiceAdaptorWiremockTest {

    public static final UUID CLAIMANT_ID = UUID.randomUUID();


    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(WiremockUtils.WIREMOCK_PORT);

    @Rule
    public WireMockClassRule wiremockServer = wireMockRule;

    @Autowired
    private CircumstancesServiceAdaptor serviceToTest;


    private CompletableFuture<Optional<Circumstances>> serviceResponse;

    @Before
    public void setup() {
        try {
            // TODO: Remove - Suggested workaround from wiremock
            // https://github.com/tomakehurst/wiremock/issues/97
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void givenTheServiceCircumstances_WeRequestUsingAnyClaimId_ThenReceive200AndData() throws ExecutionException, InterruptedException {
        givenAResponse();
        whenWeCallTheService();
        thenValuesAreTheSame();
    }

    @Test
    public void givenTheServiceCircumstances_WeRequestUsingAnyClaimId_ThenReceive200AndLocaleEnglish() throws ExecutionException, InterruptedException {
        givenAResponse();
        whenWeCallTheService();
        thenValuesLocaleIsEnglish();
    }

    private void thenValuesLocaleIsEnglish() throws ExecutionException, InterruptedException {
        Optional<Circumstances> object = serviceResponse.get();
        Assertions.assertThat(object.get().getLocale()).isEqualTo(Locale.ENGLISH.getLanguage());
    }

    private void givenAResponse() {
        WiremockUtils wiremockUtils = new WiremockUtils(wireMockRule);
        wiremockUtils.givenAResponseForCircumstances();
    }

    private void whenWeCallTheService() {
        serviceResponse = this.serviceToTest.getCircumstancesByClaimantId(CLAIMANT_ID);
    }

    private void thenValuesAreTheSame() throws ExecutionException, InterruptedException {
        Optional<Circumstances> circumstancesOpt = serviceResponse.get();
        assertNotNull("should not be null", circumstancesOpt);
        assertTrue("Should be present", circumstancesOpt.isPresent());
        Circumstances circumstances = circumstancesOpt.get();
        assertNotNull("should not be null", circumstances);
        Assertions.assertThat(circumstances.getClaimantId()).isEqualTo(StatementFactory.CLAIMANT_ID);
    }

}
