package answer.king.util;

import answer.king.throwables.exception.AnswerKingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.nio.charset.Charset;
import java.util.Properties;

import static org.springframework.http.MediaType.APPLICATION_JSON;


public class TestUtil {

    public static final MediaType JSON_UTF8_MEDIA_TYPE =
        new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    public static String convertObjectToJson(Object arg) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        try {
            json = mapper.writeValueAsString(arg);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * This method is to be used in conjunction with the controller tests for the Answer-King project. It is necessary
     * because a mapping must be specified for handling customised exceptions and interrogating the returned status
     * codes.
     *
     * @return an instance of SimpleMappingException
     */
    public static HandlerExceptionResolver getSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();

        // create mapping for all AnswerKingException subclasses
        Properties properties = new Properties();
        properties.put(AnswerKingException.class.getName(), "/exception");

        // set mappings and default the returned status code to 500
        resolver.setExceptionMappings(properties);
        resolver.setDefaultStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return resolver;
    }
}
