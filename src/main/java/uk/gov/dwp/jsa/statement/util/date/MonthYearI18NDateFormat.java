package uk.gov.dwp.jsa.statement.util.date;

import java.time.LocalDate;
import java.util.Locale;

public class MonthYearI18NDateFormat implements I18NDateFormat {
    private static final String DATE_FORMAT = "%s %s";
    private Locale locale;

    public MonthYearI18NDateFormat(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public String format(final LocalDate localDate) {
        final I18NDate i18NDate = new I18NDate(localDate, locale);
        return String.format(DATE_FORMAT, i18NDate.getMonth(), i18NDate.getYear());
    }
}
