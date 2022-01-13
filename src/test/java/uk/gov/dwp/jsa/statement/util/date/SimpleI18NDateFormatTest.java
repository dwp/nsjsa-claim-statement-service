package uk.gov.dwp.jsa.statement.util.date;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SimpleI18NDateFormatTest {

    public static final LocalDate LOCAL_DATE = LocalDate.of(2018, 9, 10);
    public static final String EXPECTED_DATE_TEXT = "Monday 10 September";

    @Test
    public void formatsDate() {
        I18NDateFormat format = new SimpleI18NDateFormat(Locale.getDefault());
        assertThat(format.format(LOCAL_DATE), is(EXPECTED_DATE_TEXT));
    }
}
