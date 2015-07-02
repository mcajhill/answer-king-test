package answer.king.controller;


import answer.king.config.AppConfig;
import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Reciept;
import answer.king.service.OrderService;
import answer.king.throwables.exception.InsufficientFundsException;
import answer.king.throwables.exception.ItemDoesNotExistException;
import answer.king.throwables.exception.OrderAlreadyPaidException;
import answer.king.throwables.exception.OrderDoesNotExistException;
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
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.List;

import static answer.king.util.ModelUtil.*;
import static answer.king.util.TestUtil.JSON_UTF8_MEDIA_TYPE;
import static answer.king.util.TestUtil.convertObjectToJson;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class OrderControllerTest {

    private final Long ORDER_ID = 1L;
    private final Long ITEM_ID = 1L;

    private final String PAY_PATH = "/order/" + ORDER_ID + "/pay/";
    private final String ADD_ITEM_PATH = "/order/" + ORDER_ID + "/addItem/" + ITEM_ID;

    @Autowired
    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    MockMvc mockMvc;


    @Before
    public void init() {
        initMocks(this);
        reset(orderService);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void getAllTest() throws Exception {
        // setup
        final MockHttpServletRequestBuilder GET_ALL_REQUEST = get("/order").contentType(JSON_UTF8_MEDIA_TYPE);

        List<Order> orders = createOrdersList();
        Long orderId = orders.get(0).getId();

        when(orderService.getAll()).thenReturn(orders);

        // execution
        mockMvc.perform(GET_ALL_REQUEST)
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(orderId.intValue())))
            .andExpect(jsonPath("$[0].items", hasSize(1)))
            .andExpect(jsonPath("$[0].items[0].price", is(1.99)))
            .andExpect(jsonPath("$[0].items[0].quantity", is(1)));

        // verification
        verify(orderService, times(1)).getAll();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    public void createTest() throws Exception {
        // setup
        final MockHttpServletRequestBuilder CREATE_REQUEST = post("/order").contentType(JSON_UTF8_MEDIA_TYPE);
        when(orderService.save(any(Order.class))).thenReturn(new Order());

        // execution and verification
        mockMvc.perform(CREATE_REQUEST)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.paid").value(false))
            .andExpect(jsonPath("$.items").doesNotExist())
            .andExpect(jsonPath("$.reciept").doesNotExist());
    }

    @Test
    public void addItemExistsTest() throws Exception {
        // setup
        Order order = createEmptyOrder(ORDER_ID);
        Item item = createBurgerItem();

        doNothing().when(orderService).addItem(order.getId(), item.getId());

        // execution and verification
        mockMvc.perform(put(ADD_ITEM_PATH))
            .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void addItemNotExistsTest() throws Exception {
        // setup
        doThrow(new ItemDoesNotExistException())
            .when(orderService).addItem(ORDER_ID, ITEM_ID);

        // execution and verification
        mockMvc.perform(put(ADD_ITEM_PATH))
            .andExpect(status().isOk());    // TODO - why not internal server error?
    }

    @Test
    public void payValidAmountTest() throws Exception {
        // setup
        BigDecimal payment = new BigDecimal("10.00");

        Order order = createBurgerOrder(ORDER_ID);
        Reciept reciept = createReciept(1L, order, payment);

        when(orderService.pay(ORDER_ID, payment)).thenReturn(reciept);

        final MockHttpServletRequestBuilder PUT_REQUEST = put(PAY_PATH).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution and verification
        double orderTotal = order.getItems().get(0).getPrice().doubleValue();
        double paid = payment.doubleValue();

        mockMvc.perform(PUT_REQUEST.content(payment.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payment", is(paid)))
            .andExpect(jsonPath("$.change", is(paid - orderTotal)));
    }

    @Test(expected = NestedServletException.class)
    public void payInvalidAmountTest() throws Exception {
        // setup
        BigDecimal payment = new BigDecimal("1.00");
        when(orderService.pay(ORDER_ID, payment)).thenThrow(new InsufficientFundsException());

        final MockHttpServletRequestBuilder PUT_REQUEST = put(PAY_PATH).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution and verification
        mockMvc.perform(PUT_REQUEST.content(payment.toString()))
            .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void payOrderNotExistsTest() throws Exception {
        // setup
        BigDecimal payment = new BigDecimal("10.00");
        when(orderService.pay(ORDER_ID, payment)).thenThrow(new OrderDoesNotExistException());

        final MockHttpServletRequestBuilder PUT_REQUEST = put(PAY_PATH).contentType(JSON_UTF8_MEDIA_TYPE);

        mockMvc.perform(PUT_REQUEST.content(payment.toString()))
            .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void payTwiceTest() throws Exception {
        // setup
        BigDecimal payment = new BigDecimal("10.00");
        when(orderService.pay(ORDER_ID, payment)).thenThrow(new OrderAlreadyPaidException());

        final MockHttpServletRequestBuilder PUT_REQUEST = put(PAY_PATH).contentType(JSON_UTF8_MEDIA_TYPE);

        mockMvc.perform(PUT_REQUEST.content(payment.toString()))
            .andExpect(status().isOk());
    }
}
