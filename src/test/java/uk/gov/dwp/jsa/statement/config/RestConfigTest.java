package uk.gov.dwp.jsa.statement.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.jsa.statement.controller.error.ErrorHandler;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestConfigTest {

    private RestConfig config;
    private RestTemplate restTemplate;
    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private ErrorHandler handler;

    @Test
    public void createsRestTemplate() {
        givenAConfig();
        whenICreateTheRestTemplate();
        thenTheRestTemplateIsCreated();
    }

    private void givenAConfig() {
        config = new RestConfig();
        when(builder.errorHandler(any())).thenReturn(builder);
        when(builder.build()).thenReturn(new RestTemplate());
    }

    private void whenICreateTheRestTemplate() {
        restTemplate = config.restTemplate(builder, handler);
    }

    private void thenTheRestTemplateIsCreated() {
        assertThat(restTemplate, is(notNullValue()));
    }
}
