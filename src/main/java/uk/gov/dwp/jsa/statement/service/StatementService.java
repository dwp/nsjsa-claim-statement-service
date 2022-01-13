package uk.gov.dwp.jsa.statement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dwp.jsa.adaptors.BankDetailsServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.CircumstancesServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.ClaimantServiceAdaptor;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.statement.domain.Statement;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class StatementService {

    private BankDetailsServiceAdaptor bankDetailsServiceAdaptor;
    private CircumstancesServiceAdaptor circumstancesServiceAdaptor;
    private ClaimantServiceAdaptor claimantServiceAdaptor;
    private StatementFactory statementFactory;

    @Autowired
    public StatementService(
            final BankDetailsServiceAdaptor bankDetailsServiceAdaptor,
            final CircumstancesServiceAdaptor circumstancesServiceAdaptor,
            final ClaimantServiceAdaptor claimantServiceAdaptor,
            final StatementFactory statementFactory) {
        this.bankDetailsServiceAdaptor = bankDetailsServiceAdaptor;
        this.circumstancesServiceAdaptor = circumstancesServiceAdaptor;
        this.claimantServiceAdaptor = claimantServiceAdaptor;
        this.statementFactory = statementFactory;
    }

    public Statement getStatement(final UUID claimantId) throws ExecutionException, InterruptedException {
        CompletableFuture<Optional<Claimant>> claimantCall = claimantServiceAdaptor.getClaimant(claimantId);
        CompletableFuture<Optional<Circumstances>> circumstancesCall =
                circumstancesServiceAdaptor.getCircumstancesByClaimantId(claimantId);
        CompletableFuture<Optional<BankDetails>> bankDetailsCall = bankDetailsServiceAdaptor
                .getBankDetailsByClaimantId(claimantId);

        CompletableFuture.allOf(claimantCall, circumstancesCall, bankDetailsCall).join();


        Optional<Claimant> claimant = claimantCall.get();
        Optional<Circumstances> circumstances = circumstancesCall.get();
        Optional<BankDetails> bankDetails = bankDetailsCall.get();
        if (claimant.isPresent() && circumstances.isPresent()) {
            return statementFactory.create(
                    claimant.get(),
                    circumstances.get(),
                    bankDetails.isPresent() ? bankDetails.get() : null);
        } else {
            return null;
        }
    }
}
