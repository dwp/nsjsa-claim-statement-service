package uk.gov.dwp.jsa.statement.util.date;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class DateSeedTest {
    private DateSeed dateSeed;
    private LocalDate now;

    @Test
    public void returnsNow() {
        givenADateSeed();
        whenIGetTheCurrentDate();
        thenADateIsCreated();
    }

    private void givenADateSeed() {
        dateSeed = new DateSeed();
    }

    private void whenIGetTheCurrentDate() {
        now = dateSeed.now();
    }

    private void thenADateIsCreated() {
        assertThat(now, is(notNullValue()));
    }
}
