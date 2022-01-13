package uk.gov.dwp.jsa.statement.domain;

import org.apache.commons.lang3.StringUtils;
import uk.gov.dwp.jsa.adaptors.dto.claim.Address;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.JuryService;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Statement {

    private LocalDate dateOfPrint;
    private final Claimant claimant;
    private final BankDetails bankDetails;
    private final Circumstances circumstances;
    private final JuryService juryService;

    public Statement(final LocalDate dateOfPrint,
                     final Claimant claimant,
                     final Circumstances circumstances,
                     final BankDetails bankDetails) {
        this.dateOfPrint = dateOfPrint;
        this.claimant = claimant;
        this.circumstances = circumstances;
        this.bankDetails = bankDetails;
        this.juryService = circumstances.getJuryService();
    }

    public LocalDate getDateOfPrint() {
        return dateOfPrint;
    }

    public Claimant getClaimant() {
        return claimant;
    }

    public Circumstances getCircumstances() {
        return circumstances;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public String getLanguage() {
        return getLocale().getDisplayLanguage();
    }

    public Boolean hasOtherBenefits() {
        return !Objects.isNull(circumstances.getOtherBenefit());
    }

    public boolean hasHadJuryService() {
        return !Objects.isNull(circumstances.getJuryService());
    }

    public boolean hasEducation() {
        return !Objects.isNull(circumstances.getEducation());
    }

    public boolean hasEmailAddress() {
        return !Objects.isNull(claimant.getContactDetails())
                && !Objects.isNull(claimant.getContactDetails().getEmail());
    }

    public boolean hasAnotherPostalAddress() {
        return !Objects.isNull(claimant.getPostalAddress());
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(circumstances.getLocale());
    }

    public JuryService getJuryService() {
        return juryService;
    }

    public String getFormattedNino() {
        String value = claimant.getNino();
        if (value == null) {
            return null;
        }
        final String nino = value.replaceAll("\\s", "");

        return IntStream.range(0, nino.length())
                .mapToObj((int i) -> {
                    if ((i % 2) != 0) {
                        return String.format("%c ", nino.charAt(i));
                    } else {
                        return String.format("%c", nino.charAt(i));
                    }
                }).collect(Collectors.joining(""));
    }

    public static String getFormattedAddress(
            final Address address) {
        return applyFormat("%s", address.getFirstLine())
                + applyFormat(", %s", address.getSecondLine())
                + applyFormat(", %s", address.getTown())
                + applyFormat(", %s", address.getPostCode());
    }

    private static String applyFormat(final String format, final String text) {
        if (!StringUtils.isBlank(text)) {
            return String.format(format, text);
        } else {
            return "";
        }
    }
}


