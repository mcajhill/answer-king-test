package answer.king.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

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
}
