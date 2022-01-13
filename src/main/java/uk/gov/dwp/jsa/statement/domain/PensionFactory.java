package uk.gov.dwp.jsa.statement.domain;


import uk.gov.dwp.jsa.adaptors.dto.claim.Address;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PensionFactory {


    public PensionDetail create(final LocalDate startDate, final String providerName,
                                final BigDecimal amount, final String paymentFrequency, final Boolean hasPeriodIncrease,
                                final String nextIncreaseMonth, final Address providerAddress) {

        return new PensionDetail(
                startDate, providerName, amount, paymentFrequency,
                hasPeriodIncrease, nextIncreaseMonth, providerAddress);
    }

}
