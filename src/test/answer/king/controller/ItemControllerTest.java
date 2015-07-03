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

import java.math.BigDecimal;

import static answer.king.util.ModelUtil.createBurgerItem;
import static answer.king.util.TestUtil.JSON_UTF8_MEDIA_TYPE;
import static answer.king.util.TestUtil.convertObjectToJson;
import static answer.king.util.TestUtil.getSimpleMappingExceptionResolver;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class ItemControllerTest {

    private final MockHttpServletRequestBuilder GET_REQUEST = get("/item").contentType(JSON_UTF8_MEDIA_TYPE);
    private final MockHttpServletRequestBuilder POST_REQUEST = post("/item").contentType(JSON_UTF8_MEDIA_TYPE);

    private final BigDecimal VALID_PRICE = new BigDecimal("10.00");
    private final BigDecimal INVALID_PRICE = VALID_PRICE.negate();

    private Item invalidItem;
    private Item validItem;
    private Long itemId;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    ItemController itemController;

    @Mock
    ItemService itemService;


    @Before
    public void setup() {
        initMocks(this);
        reset(itemService);

        mockMvc = MockMvcBuilders.standaloneSetup(itemController)
            .setHandlerExceptionResolvers(getSimpleMappingExceptionResolver())
            .build();

        invalidItem = new Item();

        validItem = createBurgerItem();
        itemId = validItem.getId();
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
        when(itemService.save(validItem)).thenReturn(validItem);

        // execution
        mockMvc.perform(POST_REQUEST
            .content(convertObjectToJson(validItem)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Burger"))
            .andExpect(jsonPath("$.price").value(1.99));

        verify(itemService, times(1)).save(validItem);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void createWithInvalidNameTest() throws Exception {
        // setup
        when(itemService.save(invalidItem))
            .thenThrow(new InvalidItemNameException());

        // execution
        mockMvc.perform(POST_REQUEST
            .content(convertObjectToJson(invalidItem)))
            .andExpect(status().isInternalServerError());

        // verification
        verify(itemService, times(1)).save(invalidItem);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void updateValidPriceTest() throws Exception {
        // setup
        validItem.setPrice(VALID_PRICE);

        when(itemService.updatePrice(itemId, VALID_PRICE))
            .thenReturn(validItem);

        String path = "/item/" + itemId + "/updatePrice";
        MockHttpServletRequestBuilder PUT_REQUEST = put(path).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution
        mockMvc.perform(PUT_REQUEST
            .content(convertObjectToJson(VALID_PRICE)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
            .andExpect(jsonPath("$.price", is(VALID_PRICE.doubleValue())));

        // verification
        verify(itemService, times(1)).updatePrice(itemId, VALID_PRICE);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void updateInvalidPriceTest() throws Exception {
        // setup
        validItem.setPrice(INVALID_PRICE);

        when(itemService.updatePrice(itemId, INVALID_PRICE))
            .thenThrow(new InvalidItemPriceException());

        String path = "/item/" + itemId + "/updatePrice";
        MockHttpServletRequestBuilder PUT_REQUEST = put(path).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution
        mockMvc.perform(PUT_REQUEST
            .contentType(JSON_UTF8_MEDIA_TYPE)
            .content(convertObjectToJson(INVALID_PRICE)))
            .andExpect(status().isInternalServerError());

        verify(itemService, times(1)).updatePrice(itemId, INVALID_PRICE);
        verifyNoMoreInteractions(itemService);
    }
}
