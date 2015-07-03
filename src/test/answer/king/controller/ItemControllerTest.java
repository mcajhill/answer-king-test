package answer.king.controller;

import answer.king.config.AppConfig;
import answer.king.model.Item;
import answer.king.service.ItemService;
import answer.king.throwables.exception.InvalidItemNameException;
import answer.king.throwables.exception.InvalidItemPriceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;

import static answer.king.util.ModelUtil.createBurgerItem;
import static answer.king.util.TestUtil.JSON_UTF8_MEDIA_TYPE;
import static answer.king.util.TestUtil.convertObjectToJson;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class ItemControllerTest {

    private final MockHttpServletRequestBuilder GET_REQUEST = get("/item").contentType(JSON_UTF8_MEDIA_TYPE);
    private final MockHttpServletRequestBuilder POST_REQUEST = post("/item").contentType(JSON_UTF8_MEDIA_TYPE);

    private MockMvc mockMvc;
    private Item item;

    @Autowired
    @InjectMocks
    ItemController itemController;

    @Mock
    ItemService itemService;


    @Before
    public void setup() {
        initMocks(this);
        reset(itemService);

        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        item = createBurgerItem();
    }

    @Test
    public void getAllTest() throws Exception {
        // execution
        mockMvc.perform(GET_REQUEST)
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE));

        // verification
        verify(itemService, times(1)).getAll();
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void createWithValidNameTest() throws Exception {
        // setup
        String itemJson = convertObjectToJson(item);
        when(itemService.save(item)).thenReturn(item);

        // execution
        mockMvc.perform(POST_REQUEST.content(itemJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Burger"))
            .andExpect(jsonPath("$.price").value(1.99));

        verify(itemService, times(1)).save(item);
        verifyNoMoreInteractions(itemService);
    }

    @Test(expected = NestedServletException.class)
    public void createWithInvalidNameTest() throws Exception {
        // setup
        Item invalidItem = new Item();
        String invalidItemJson = convertObjectToJson(invalidItem);

        when(itemService.save(invalidItem)).thenThrow(new InvalidItemNameException());

        // execution
        mockMvc.perform(POST_REQUEST.content(invalidItemJson))
            .andExpect(status().isInternalServerError());

        // verification
        verify(itemService, times(1)).save(invalidItem);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void updateValidPriceTest() throws Exception {
        // setup
        Long itemId = item.getId();

        BigDecimal updatedPrice = new BigDecimal("10.00");
        item.setPrice(updatedPrice);

        when(itemService.updatePrice(itemId, updatedPrice)).thenReturn(item);

        String path = "/item/" + itemId + "/updatePrice";
        String content = convertObjectToJson(updatedPrice);

        MockHttpServletRequestBuilder PUT_REQUEST = put(path).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution
        mockMvc.perform(PUT_REQUEST.content(content))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price", is(updatedPrice.doubleValue())));

        // verification
        verify(itemService, times(1)).updatePrice(itemId, updatedPrice);
        verifyNoMoreInteractions(itemService);
    }

    @Test(expected = NestedServletException.class)
    public void updateInvalidPriceTest() throws Exception {
        // setup
        Long itemId = item.getId();

        BigDecimal invalidPrice = BigDecimal.TEN.negate();
        item.setPrice(invalidPrice);

        when(itemService.updatePrice(itemId, invalidPrice)).thenThrow(new InvalidItemPriceException());

        String path = "/item/" + itemId + "/updatePrice";
        String content = convertObjectToJson(invalidPrice);

        MockHttpServletRequestBuilder PUT_REQUEST = put(path).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution
        mockMvc.perform(PUT_REQUEST.content(content))
            .andExpect(status().isInternalServerError());

        verify(itemService, times(1)).updatePrice(itemId, invalidPrice);
        verifyNoMoreInteractions(itemService);
    }
}
