package answer.king.controller;

import answer.king.config.AppConfig;
import answer.king.model.Item;
import answer.king.service.ItemService;
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

import java.math.BigDecimal;

import static answer.king.util.TestUtil.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class ItemControllerTest {

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

        item = new Item();
        item.setName("Burger");
        item.setPrice(new BigDecimal("1.99"));
    }

    @Test
    public void getAllTest() throws Exception {
        // setup
        MockHttpServletRequestBuilder getRequest = get("/item").contentType(JSON_UTF8_MEDIA_TYPE);

        // execution
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE));

        // verification
        verify(itemService, times(1)).getAll();
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void createTest() throws Exception {
        // setup
        MockHttpServletRequestBuilder postRequest = post("/item").contentType(JSON_UTF8_MEDIA_TYPE);
        String itemJson = convertItemToJson(item);

        Item allocatedItem = item;
        item.setId(1L);

        when(itemService.save(item)).thenReturn(allocatedItem);

        // execution
        MvcResult result =
            mockMvc.perform(postRequest.content(itemJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Item parsedItem = convertJsonToItem(responseBody);

        // verification
        assertNotNull(parsedItem);
        assertNotNull(allocatedItem.getId());
        verify(itemService, times(1)).save(item);
        verifyNoMoreInteractions(itemService);
    }
}
