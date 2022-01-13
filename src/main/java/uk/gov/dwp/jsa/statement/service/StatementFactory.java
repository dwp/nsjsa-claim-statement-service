package uk.gov.dwp.jsa.statement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.statement.domain.Statement;
import uk.gov.dwp.jsa.statement.util.date.DateSeed;

@Component
public class StatementFactory {
    private DateSeed dateSeed;

    @Autowired
    public StatementFactory(final DateSeed dateSeed) {

        this.dateSeed = dateSeed;
    }

    public Statement create(final Claimant claimant,
                            final Circumstances circumstances,
                            final BankDetails bankDetails) {
        return new Statement(dateSeed.now(),
                             claimant,
                             circumstances,
                             bankDetails);
    }
}
