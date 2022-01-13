package uk.gov.dwp.jsa.statement.acceptance_test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import  uk.gov.dwp.jsa.adaptors.ServicesProperties;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServicesPropertiesAcceptanceTest {

    @Autowired
    private ServicesProperties servicesProperties;

    @Test
    public void testPropertiesShouldNotBeNull() {
        assertNotNull("Should not be null", servicesProperties.getBankDetailsServer());

        assertNotNull("Should not be null", servicesProperties.getCircumstancesServer());

        assertNotNull("Should not be null", servicesProperties.getClaimantServer());

        assertNotNull("Should not be null", servicesProperties.getClaimantVersion());

        assertNotNull("Should not be null", servicesProperties.getCircumstancesVersion());

        assertNotNull("Should not be null", servicesProperties.getBankDetailsVersion());
    }
}
