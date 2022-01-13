package uk.gov.dwp.jsa.statement.acceptance_test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import uk.gov.dwp.jsa.statement.util.date.DateSeed;

import java.time.LocalDate;
import java.util.Locale;

@Configuration
@Profile("local_test")
public class TestConfig {
    @Bean
    public DateSeed dateSeed() {
        return new TestDateSeed();
    }

    public static class TestDateSeed extends DateSeed {
        private static final LocalDate DATE_OF_PRINT = LocalDate.of(2018, 10, 12);

        @Override
        public LocalDate now() {
            return DATE_OF_PRINT;
        }
    }

    @Bean
    LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        return resolver;
    }


}
