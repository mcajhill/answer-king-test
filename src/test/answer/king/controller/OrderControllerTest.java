package answer.king.controller;


import answer.king.config.AppConfig;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static answer.king.util.ModelUtil.createBurgerOrder;
import static answer.king.util.ModelUtil.createOrdersList;
import static answer.king.util.ModelUtil.createReciept;
import static answer.king.util.TestUtil.JSON_UTF8_MEDIA_TYPE;
import static answer.king.util.TestUtil.convertObjectToJson;
import static answer.king.util.TestUtil.getSimpleMappingExceptionResolver;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
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
public class OrderControllerTest {

    private final Long ORDER_ID = 1L;
    private final Long ITEM_ID = 1L;

    private final String PAY_PATH = "/order/" + ORDER_ID + "/pay/";
    private final String ADD_ITEM_PATH = "/order/" + ORDER_ID + "/addItem/" + ITEM_ID;

    private final MockHttpServletRequestBuilder GET_ALL_REQUEST = get("/order").contentType(JSON_UTF8_MEDIA_TYPE);
    private final MockHttpServletRequestBuilder CREATE_REQUEST = post("/order").contentType(JSON_UTF8_MEDIA_TYPE);
    private final MockHttpServletRequestBuilder ADD_ITEM_REQUEST = put(ADD_ITEM_PATH).contentType(JSON_UTF8_MEDIA_TYPE);
    private final MockHttpServletRequestBuilder PAY_REQUEST = put(PAY_PATH).contentType(JSON_UTF8_MEDIA_TYPE);

    private final BigDecimal VALID_PAYMENT = new BigDecimal("10.00");
    private final BigDecimal INVALID_PAYMENT = new BigDecimal("1.00");

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;


    @Before
    public void init() {
        initMocks(this);
        reset(orderService);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
            .setHandlerExceptionResolvers(getSimpleMappingExceptionResolver())
            .build();
    }

    @Test
    public void getAllTest() throws Exception {
        // setup
        List<Order> orders = createOrdersList();
        when(orderService.getAll()).thenReturn(orders);

        // execution
        mockMvc.perform(GET_ALL_REQUEST)
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(ORDER_ID.intValue())))
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
        when(orderService.save(any(Order.class))).thenReturn(new Order());

        // execution and verification
        mockMvc.perform(CREATE_REQUEST)
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.paid").value(false))
            .andExpect(jsonPath("$.items").doesNotExist())
            .andExpect(jsonPath("$.reciept").doesNotExist());
    }

    @Test
    public void addValidItemTest() throws Exception {
        // execution and verification
        mockMvc.perform(ADD_ITEM_REQUEST
            .content(convertObjectToJson(1)))
            .andExpect(status().isOk());
    }

    @Test //(expected = ItemDoesNotExistException.class)
    public void addItemNotExistsTest() throws Exception {
        // setup
        doThrow(new ItemDoesNotExistException())
            .when(orderService).addItem(ORDER_ID, ITEM_ID, 1);

        // execution and verification
        mockMvc.perform(ADD_ITEM_REQUEST
            .content(convertObjectToJson(1)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void payValidAmountTest() throws Exception {
        // setup
        Order order = createBurgerOrder(ORDER_ID);
        Reciept reciept = createReciept(1L, order, VALID_PAYMENT);

        when(orderService.pay(ORDER_ID, VALID_PAYMENT)).thenReturn(reciept);

        // execution and verification
        double orderTotal = order.getItems().get(0).getPrice().doubleValue();
        double paid = VALID_PAYMENT.doubleValue();

        mockMvc.perform(PAY_REQUEST
            .content(convertObjectToJson(VALID_PAYMENT)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(JSON_UTF8_MEDIA_TYPE))
            .andExpect(jsonPath("$.payment", is(paid)))
            .andExpect(jsonPath("$.change", is(paid - orderTotal)));
    }

    @Test
    public void payInvalidAmountTest() throws Exception {
        // setup
        when(orderService.pay(ORDER_ID, INVALID_PAYMENT))
            .thenThrow(new InsufficientFundsException());

        // execution and verification
        mockMvc.perform(PAY_REQUEST
            .content(convertObjectToJson(INVALID_PAYMENT)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void payOrderNotExistsTest() throws Exception {
        // setup
        when(orderService.pay(ORDER_ID, VALID_PAYMENT))
            .thenThrow(new OrderDoesNotExistException());

        // execution and verification
        mockMvc.perform(PAY_REQUEST
            .content(convertObjectToJson(VALID_PAYMENT)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void payTwiceTest() throws Exception {
        // setup
        when(orderService.pay(ORDER_ID, VALID_PAYMENT))
            .thenThrow(new OrderAlreadyPaidException());

        // execution and verification
        mockMvc.perform(PAY_REQUEST
            .content(convertObjectToJson(VALID_PAYMENT)))
            .andExpect(status().isInternalServerError());
    }
}
