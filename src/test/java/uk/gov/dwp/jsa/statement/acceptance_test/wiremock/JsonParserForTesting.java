package uk.gov.dwp.jsa.statement.acceptance_test.wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonParserForTesting {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParserForTesting.class);

    public String build(Object valuesToParse){
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        objectMapper.setDateFormat(new StdDateFormat());
        String jsonInString = null;
        try {
            jsonInString = objectMapper.writeValueAsString(valuesToParse);
        } catch (JsonProcessingException e) {
            LOGGER.error("PARSING the object"+ valuesToParse.toString(), e);
        }
        return jsonInString;
    }
}
