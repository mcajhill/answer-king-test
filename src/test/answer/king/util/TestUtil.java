package answer.king.util;

import answer.king.model.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

import static org.springframework.http.MediaType.APPLICATION_JSON;


public class TestUtil {

    public static final MediaType JSON_UTF8_MEDIA_TYPE =
        new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    public static String convertItemToJson(Item item) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        try {
            json = mapper.writeValueAsString(item);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static Item convertJsonToItem(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Item item = null;

        try {
            item = mapper.readValue(json, Item.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }
}
