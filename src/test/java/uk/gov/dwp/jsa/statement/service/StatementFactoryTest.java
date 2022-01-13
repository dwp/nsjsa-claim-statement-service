package uk.gov.dwp.jsa.statement.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.statement.domain.Statement;
import uk.gov.dwp.jsa.statement.util.date.DateSeed;

import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatementFactoryTest {

    private static final BankDetails BANK_DETAILS = new BankDetails();
    private static final Claimant CLAIMANT = new Claimant();
    private static final Locale LOCALE = Locale.getDefault();
    private static final LocalDate DATE_OF_PRINT = LocalDate.now();

    @Mock
    private DateSeed dateSeed;

    @Mock
    private Circumstances mockedCircumstances;

    private StatementFactory statementFactory;
    private Statement statement;

    @Before
    public void beforeEachTest() {
        initMocks(this);
        when(mockedCircumstances.getLocale()).thenReturn(Locale.ENGLISH.getLanguage());
    }

    @Test
    public void createsStatement() {
        givenAFactory();
        whenICallCreate();
        thenTheStatementIsCreated();
    }

    private void givenAFactory() {
        statementFactory = new StatementFactory(dateSeed);
        when(dateSeed.now()).thenReturn(DATE_OF_PRINT);
    }

    private void whenICallCreate() {
        statement = statementFactory.create(CLAIMANT, mockedCircumstances, BANK_DETAILS);
    }

    private void thenTheStatementIsCreated() {
        assertThat(statement.getBankDetails(), is(BANK_DETAILS));
        assertThat(statement.getCircumstances(), is(mockedCircumstances));
        assertThat(statement.getClaimant(), is(CLAIMANT));
        assertThat(statement.getDateOfPrint(), is(DATE_OF_PRINT));
        assertThat(statement.getLanguage(), is(LOCALE.getDisplayLanguage()));
    }
}
