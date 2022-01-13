package uk.gov.dwp.jsa.statement.acceptance_test.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.adaptors.http.api.ApiSuccess;
import uk.gov.dwp.jsa.adaptors.http.api.BankDetailsResponse;
import uk.gov.dwp.jsa.adaptors.http.api.CircumstancesResponse;
import uk.gov.dwp.jsa.adaptors.http.api.ClaimantResponse;
import uk.gov.dwp.jsa.statement.domain.Statement;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

public class WiremockUtils {

    public static final Integer WIREMOCK_PORT = 9090;

    private static final URI URI_SENT = URI.create("DA_URI");

   private WireMockClassRule wiremockServer;

    private static final String GET_CLAIMANT_BY_CLAIM_ID_URL = "/nsjsa/v([0-9]*)/citizen/([a-zA-Z0-9\\-]*)";;

    private static final String GET_BANK_DETAILS_BY_CLAIM_ID_URL = "/nsjsa/v([0-9]*)/claim/([a-zA-Z0-9\\-]*)" +
            "/bank-details";

    private static final String GET_CIRCUMSTANCES_BY_CLAIM_ID_URL = "/nsjsa/v([0-9]*)/citizen/([a-zA-Z0-9\\-]*)/claim";

    public WiremockUtils(WireMockClassRule wiremockServer) {
        this.wiremockServer = wiremockServer;
    }



    private static StatementFactory deafaultValuesFactory;

    private BankDetailsResponse createBankDetailsResponseWithSuccess(BankDetails bankDetails){
        BankDetailsResponse response = new BankDetailsResponse();
        ApiSuccess<BankDetails> apiSuccess = new ApiSuccess<>();
        apiSuccess.setData(bankDetails);
        List<ApiSuccess<BankDetails>> list = Arrays.asList(apiSuccess);
        response.setSuccess(list);
        return response;
    }

    private String createBasicSuccessBankDetailsResponse(){
         BankDetails bankDetailsToBeMocked;
        JsonParserForTesting parser = new JsonParserForTesting();
        bankDetailsToBeMocked = createStatementDefaultValues().getBankDetails();
        return parser.build(createBankDetailsResponseWithSuccess(bankDetailsToBeMocked));
    }

    private Statement createStatementDefaultValues(){
        deafaultValuesFactory = new StatementFactory();
        return deafaultValuesFactory.create();
    }


    private CircumstancesResponse createCircumstancesResponseWithSuccessForCircumstances(Circumstances circumstances){
        CircumstancesResponse response = new CircumstancesResponse();
        ApiSuccess<Circumstances> apiSuccess = new ApiSuccess<>();
        apiSuccess.setData(circumstances);
        List<ApiSuccess<Circumstances>> list = Arrays.asList(apiSuccess);
        response.setSuccess(list);
        return response;
    }

    private String createBasicSuccessCircumstancesResponse(){
        Circumstances circumstancesToBeMocked;
        JsonParserForTesting parser = new JsonParserForTesting();
        circumstancesToBeMocked = createStatementDefaultValues().getCircumstances();
        return parser.build(createCircumstancesResponseWithSuccessForCircumstances(circumstancesToBeMocked));
    }

    private ClaimantResponse createClaimantDetailsResponseWithSuccessForClaimant(Claimant claimant){
        ClaimantResponse response = new ClaimantResponse();
        ApiSuccess<Claimant> apiSuccess = new ApiSuccess<>();
        apiSuccess.setData(claimant);
        List<ApiSuccess<Claimant>> list = Arrays.asList(apiSuccess);
        response.setSuccess(list);
        return response;

    }

    private String createBasicSuccessClaimantResponse(){
        JsonParserForTesting parser = new JsonParserForTesting();
        Claimant claimantToBeMocked = createStatementDefaultValues().getClaimant();
        claimantToBeMocked.setClaimantId(StatementFactory.CLAIMANT_ID);
        return parser.build(createClaimantDetailsResponseWithSuccessForClaimant(claimantToBeMocked));
    }

    public String createBasicSuccessCircumstancesResponseWithLocaleWelsh() {
        Circumstances circumstancesToBeMocked;
        JsonParserForTesting parser = new JsonParserForTesting();
        circumstancesToBeMocked = createStatementDefaultValues().getCircumstances();
        circumstancesToBeMocked.setLocale("cy");
        return parser.build(createCircumstancesResponseWithSuccessForCircumstances(circumstancesToBeMocked));
    }


    public void givenAResponseForBankDetails() {
        wiremockServer.stubFor(WireMock.get(urlPathMatching(GET_BANK_DETAILS_BY_CLAIM_ID_URL)).willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withStatus(200)
                .withBody(createBasicSuccessBankDetailsResponse())));
    }

    public void givenA404ForBankDetails() {
        wiremockServer.stubFor(WireMock.get(urlPathMatching(GET_BANK_DETAILS_BY_CLAIM_ID_URL)).willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withStatus(404)));
    }


    public void givenAResponseForCircumstances() {
        wiremockServer.stubFor(WireMock.get(urlPathMatching(GET_CIRCUMSTANCES_BY_CLAIM_ID_URL)).willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(createBasicSuccessCircumstancesResponse())));
    }


    public void givenAResponseForCircumstancesInWelsh() {
        wiremockServer.stubFor(WireMock.get(urlPathMatching(GET_CIRCUMSTANCES_BY_CLAIM_ID_URL)).willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(createBasicSuccessCircumstancesResponseWithLocaleWelsh())));
    }


    public void givenAResponseForClaimant() {
        wiremockServer.stubFor(WireMock.get(urlPathMatching(GET_CLAIMANT_BY_CLAIM_ID_URL)).willReturn(aResponse()
                .withHeader("Content-Type", APPLICATION_JSON)
                .withStatus(200)
                .withBody(createBasicSuccessClaimantResponse())));
    }


}
