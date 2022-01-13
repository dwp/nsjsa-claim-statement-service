package uk.gov.dwp.jsa.statement.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dwp.jsa.adaptors.dto.claim.Address;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.ContactDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Education;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.JuryService;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.OtherBenefit;

import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatementTest {

    private BankDetails bankDetails;
    @Mock
    private Circumstances circumstances;
    private Claimant claimant;
    private Locale locale = Locale.ENGLISH;
    private LocalDate dateOfPrint;

    private Statement statement;

    private String formattedNino;

    private boolean state;

    @Before
    public void beforeEachTest() {
        initMocks(this);
        bankDetails = new BankDetails();
        claimant = new Claimant();
        locale = Locale.getDefault();
        dateOfPrint = LocalDate.now();
        circumstances = new Circumstances();
        circumstances.setLocale(locale.getLanguage());
    }

    @Test
    public void constructorSetsFields() {
        Statement statement = new Statement(
                dateOfPrint,
                claimant,
                circumstances,
                bankDetails);
        assertThat(statement.getLanguage(), is(locale.getDisplayLanguage()));
        assertThat(statement.getDateOfPrint(), is(dateOfPrint));
        assertThat(statement.getClaimant(), is(claimant));
        assertThat(statement.getCircumstances(), is(circumstances));
        assertThat(statement.getBankDetails(), is(bankDetails));
        assertThat(statement.getLocale().getLanguage(), is(locale.getLanguage()));
    }

    @Test
    public void givenUnformattedAddress_withNoSecondLine_returnsFormattedAddress() {
        Address address = new Address();
        address.setFirstLine("1 somestreet");
        address.setTown("sometown");
        address.setPostCode("NE1 5UN");
        String expected = address.getFirstLine() + ", " + address.getTown() + ", " + address.getPostCode();

        assertEquals(expected, statement.getFormattedAddress(address));
    }

    @Test
    public void givenUnformattedAddress_withSecondLine_returnsFormattedAddress() {
        Address address = new Address();
        address.setFirstLine("1 somestreet");
        address.setSecondLine("secondline");
        address.setTown("sometown");
        address.setPostCode("NE1 5UN");
        String expected = address.getFirstLine() + ", " + address.getSecondLine() + ", " +
                address.getTown() + ", " + address.getPostCode();

        assertEquals(expected, statement.getFormattedAddress(address));
    }

    @Test
    public void givenUnformattedNino_returnWellFormattedNino() {
        givenIHaveAClaimantWithANino("SN123456A");
        whenIFormattedANino();
        thenIExpectTheNinoToBe("SN 12 34 56 A");
    }

    @Test
    public void formatsNinoWithBlankSpacesInIt() {
        givenIHaveAClaimantWithANino("n c                          13783 8c");
        whenIFormattedANino();
        thenIExpectTheNinoToBe("nc 13 78 38 c");
    }

    @Test
    public void getFormmattedNinoWithNullReturnsNull() {
        givenIHaveAClaimantWithANino(null);
        whenIFormattedANino();
        thenIExpectTheNinoToBe(null);
    }

    @Test
    public void whenWeHaveAOtherBenefitsReturnIsTrue() {
        givenWeHaveOtherBenefitOf(new OtherBenefit());
        whenWeCheckOtherBenefits();
        thenWeExpectItToBe(true);
    }

    @Test
    public void whenWeHaveNoOtherBenefitsReturnIsFalse() {
        givenWeHaveOtherBenefitOf(null);
        whenWeCheckOtherBenefits();
        thenWeExpectItToBe(false);
    }

    @Test
    public void whenWeHaveAJuryServiceReturnIsTrue() {
        givenWeHaveJuryServiceOf(new JuryService());
        whenWeCheckJuryService();
        thenWeExpectItToBe(true);
    }

    @Test
    public void whenWeHaveNoJuryServiceReturnIsFalse() {
        givenWeHaveJuryServiceOf(null);
        whenWeCheckJuryService();
        thenWeExpectItToBe(false);
    }

    @Test
    public void whenWeHaveAEducationReturnIsTrue() {
        givenWeHaveEducationOf(new Education());
        whenWeCheckEducation();
        thenWeExpectItToBe(true);
    }

    @Test
    public void whenWeHaveNoEducationReturnIsFalse() {
        givenWeHaveEducationOf(null);
        whenWeCheckEducation();
        thenWeExpectItToBe(false);
    }

    @Test
    public void whenWeHaveAEmailReturnIsTrue() {
        givenWeHaveContactDetailsOf(new ContactDetails(null, "email", true, true));
        whenWeCheckEmail();
        thenWeExpectItToBe(true);
    }

    @Test
    public void whenWeHaveNoEmailReturnIsFalse() {
        givenWeHaveContactDetailsOf(new ContactDetails(null, null, true, false));
        whenWeCheckEmail();
        thenWeExpectItToBe(false);
    }

    @Test
    public void whenWeHaveNoContactDetailsReturnIsFalse() {
        givenWeHaveContactDetailsOf(null);
        whenWeCheckEmail();
        thenWeExpectItToBe(false);
    }

    @Test
    public void whenWeHaveAAnotherPostalAddressReturnIsTrue() {
        givenWeHaveAnotherPostalAddressOf(new Address());
        whenWeCheckAnotherPostalAddress();
        thenWeExpectItToBe(true);
    }

    @Test
    public void whenWeHaveNoAnotherPostalAddressReturnIsFalse() {
        givenWeHaveAnotherPostalAddressOf(null);
        whenWeCheckAnotherPostalAddress();
        thenWeExpectItToBe(false);
    }

    private void givenIHaveAClaimantWithANino(final String nino) {
        claimant.setNino(nino);
        statement = new Statement( dateOfPrint, claimant, circumstances, bankDetails);
    }
    private void givenWeHaveOtherBenefitOf(OtherBenefit otherBenefit) {
        circumstances.setOtherBenefit(otherBenefit);
        statement = new Statement( dateOfPrint, claimant, circumstances, bankDetails);
    }
    private void givenWeHaveEducationOf(final Education education) {
        circumstances.setEducation(education);
        statement = new Statement( dateOfPrint, claimant, circumstances, bankDetails);
    }
    private void givenWeHaveJuryServiceOf(final JuryService juryService) {
        circumstances.setJuryService(juryService);
        statement = new Statement( dateOfPrint, claimant, circumstances, bankDetails);
    }
    private void givenWeHaveContactDetailsOf(final ContactDetails contactDetails) {
        claimant.setContactDetails(contactDetails);
        statement = new Statement( dateOfPrint, claimant, circumstances, bankDetails);
    }
    private void givenWeHaveAnotherPostalAddressOf(final Address postalAddress) {
        claimant.setPostalAddress(postalAddress);
        statement = new Statement( dateOfPrint, claimant, circumstances, bankDetails);
    }

    private void whenIFormattedANino() {
        formattedNino = statement.getFormattedNino();
    }
    private void whenWeCheckOtherBenefits() {
        state = statement.hasOtherBenefits();
    }
    private void whenWeCheckEducation() {
        state = statement.hasEducation();
    }
    private void whenWeCheckJuryService() {
        state = statement.hasHadJuryService();
    }
    private void whenWeCheckEmail() {
        state = statement.hasEmailAddress();
    }
    private void whenWeCheckAnotherPostalAddress() {
        state = statement.hasAnotherPostalAddress();
    }

    private void thenIExpectTheNinoToBe(String expect) {
        MatcherAssert.assertThat(formattedNino, Matchers.is(expect));
    }

    private void thenWeExpectItToBe(final boolean expected) {
        assertThat(state, is(expected));
    }
}
