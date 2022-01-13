package uk.gov.dwp.jsa.statement.util.date;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MonthYearI18NDateFormatTest {

    public static final LocalDate LOCAL_DATE = LocalDate.of(2018, 9, 10);
    public static final String EXPECTED_DATE_TEXT = "September 2018";

    @Test
    public void formatsDate() {
        I18NDateFormat format = new MonthYearI18NDateFormat(Locale.getDefault());
        assertThat(format.format(LOCAL_DATE), is(EXPECTED_DATE_TEXT));
    }
}
