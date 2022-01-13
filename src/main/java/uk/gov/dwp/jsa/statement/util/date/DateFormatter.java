package uk.gov.dwp.jsa.statement.util.date;

import java.time.LocalDate;

public interface DateFormatter {
    String format(final LocalDate date);
}
