package uk.gov.dwp.jsa.statement.util.date;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SimpleDateFormatterTest {

    private static final LocalDate DATE = LocalDate.of(2016, 11, 1);
    private static final String FORMATTED_DATE = "01/11/2016";
    private SimpleDateFormatter formatter;
    private String formattedDate;

    @Test
    public void formatsDate() {
        givenAFormatter();
        whenIFormatTheDate();
        thenTheDateIsFormatted();
    }

    private void givenAFormatter() {
        formatter = new SimpleDateFormatter();
    }

    private void whenIFormatTheDate() {
        formattedDate = formatter.format(DATE);
    }

    private void thenTheDateIsFormatted() {
        assertThat(formattedDate, is(FORMATTED_DATE));
    }
}
