package uk.gov.dwp.jsa.statement.util.date;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("!local_test")
public class DateSeed {
    public LocalDate now() {
        return LocalDate.now();
    }
}
