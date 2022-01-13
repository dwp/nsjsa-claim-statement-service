package uk.gov.dwp.jsa.statement.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.jsa.statement.controller.error.ErrorHandler;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder, final ErrorHandler errorHandler) {
        return builder.errorHandler(errorHandler).build();
    }
}
