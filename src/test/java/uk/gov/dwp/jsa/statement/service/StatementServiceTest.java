package uk.gov.dwp.jsa.statement.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dwp.jsa.adaptors.BankDetailsServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.CircumstancesServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.ClaimantServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.JuryService;
import uk.gov.dwp.jsa.statement.domain.Statement;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.util.Optional.of;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatementServiceTest {

    private static final BankDetails BANK_DETAILS = new BankDetails();
    private static final Circumstances CIRCUMSTANCES = new Circumstances();
    private static final Claimant CLAIMANT = new Claimant();
    private static final LocalDate DATE_OF_PRINT = LocalDate.now();
    private static final Statement STATEMENT = new Statement(
            DATE_OF_PRINT, CLAIMANT, CIRCUMSTANCES, BANK_DETAILS);
    private static final UUID CLAIMANT_ID = UUID.randomUUID();

    @Mock
    private BankDetailsServiceAdaptor bankDetailsServiceAdaptor;
    @Mock
    private CircumstancesServiceAdaptor circumstancesServiceAdaptor;
    @Mock
    private ClaimantServiceAdaptor claimantServiceAdaptor;
    @Mock
    private StatementFactory statementFactory;

    private StatementService statementService;
    private Statement statement;

    @Before
    public void beforeEachTest() {
        initMocks(this);
    }

    @Test
    public void createsStatement() throws ExecutionException, InterruptedException {
        givenAStatementService();
        whenIGetTheStatement();
        thenTheStatementIsCreated();
        andJuryServiceIsNull();
    }

    private void givenAStatementService() {
        statementService = new StatementService(bankDetailsServiceAdaptor,
                circumstancesServiceAdaptor,
                claimantServiceAdaptor,
                statementFactory);
        when(bankDetailsServiceAdaptor.getBankDetailsByClaimantId(CLAIMANT_ID)).thenReturn(completedFuture(of(BANK_DETAILS)));
        when(circumstancesServiceAdaptor.getCircumstancesByClaimantId(CLAIMANT_ID)).thenReturn(completedFuture(of(CIRCUMSTANCES)));
        when(claimantServiceAdaptor.getClaimant(CLAIMANT_ID)).thenReturn(completedFuture(of(CLAIMANT)));
        when(statementFactory.create(
                CLAIMANT,
                CIRCUMSTANCES,
                BANK_DETAILS)).thenReturn(STATEMENT);
    }

    private void whenIGetTheStatement() throws ExecutionException, InterruptedException {
        statement = statementService.getStatement(CLAIMANT_ID);
    }

    private void thenTheStatementIsCreated() {
        assertThat(statement, is(STATEMENT));
    }

    private void andJuryServiceIsNull() {
        final JuryService juryService = statement.getJuryService();
        assertNull(juryService);
    }
}
