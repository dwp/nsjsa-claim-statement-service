package uk.gov.dwp.jsa.statement.util.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SimpleDateFormatter implements DateFormatter {
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    public String format(final LocalDate date) {
        return DateTimeFormatter.ofPattern(DATE_FORMAT).format(date);
    }
}
