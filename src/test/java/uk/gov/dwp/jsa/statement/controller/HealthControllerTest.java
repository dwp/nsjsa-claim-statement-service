package uk.gov.dwp.jsa.statement.controller;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class HealthControllerTest {

    public static final String PONG = "pong";

    private HealthController controller;
    private ResponseEntity<String> response;

    @Test
    public void pings() {
        givenAController();
        whenICallPing();
        thenThePingResponds();
    }

    private void givenAController() {
        controller = new HealthController();
    }

    private void whenICallPing() {
        response = controller.ping();
    }

    private void thenThePingResponds() {
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(PONG));
    }

}
