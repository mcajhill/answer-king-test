package answer.king.controller;

import answer.king.config.AppConfig;
import answer.king.model.Item;
import answer.king.service.ItemService;
import answer.king.throwables.exception.InvalidItemException;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static answer.king.util.ModelUtil.createBurgerItem;
import static answer.king.util.TestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class ItemControllerTest {

    private final MockHttpServletRequestBuilder POST_REQUEST = post("/item").contentType(JSON_UTF8_MEDIA_TYPE);
    private final MockHttpServletRequestBuilder GET_REQUEST = get("/item").contentType(JSON_UTF8_MEDIA_TYPE);

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
        item = createBurgerItem(null);
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
    public void createWithValidItemTest() throws Exception {
        // setup
        String itemJson = convertObjectToJson(item);
        when(itemService.save(item)).thenReturn(item);

        // execution
        mockMvc.perform(POST_REQUEST.content(itemJson))
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Burger"))
            .andExpect(jsonPath("$.price").value(1.99));

        verify(itemService, times(1)).save(item);
        verifyNoMoreInteractions(itemService);
    }

    @Test(expected = NestedServletException.class)
    public void createWithInvalidItemTest() throws Exception {
        // setup
        Item invalidItem = new Item();
        String invalidItemJson = convertObjectToJson(invalidItem);

        InvalidItemException exception = new InvalidItemException("Item must have a valid name and price");
        when(itemService.save(invalidItem)).thenThrow(exception);

        // execution
        mockMvc.perform(POST_REQUEST.content(invalidItemJson))
            .andExpect(status().isInternalServerError());

        // verification
        verify(itemService, times(1)).save(invalidItem);
        verifyNoMoreInteractions(itemService);
    }
}
