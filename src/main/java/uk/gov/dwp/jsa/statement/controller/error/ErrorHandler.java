package uk.gov.dwp.jsa.statement.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Component
public class ErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public boolean hasError(final ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        return statusCode != null && statusCode.series() == HttpStatus.Series.SERVER_ERROR;
    }
}
