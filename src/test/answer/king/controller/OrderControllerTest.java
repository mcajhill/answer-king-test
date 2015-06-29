package answer.king.controller;


import answer.king.config.AppConfig;
import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Reciept;
import answer.king.service.OrderService;
import answer.king.throwables.exception.InsufficientFundsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.List;

import static answer.king.util.ModelUtil.*;
import static answer.king.util.TestUtil.JSON_UTF8_MEDIA_TYPE;
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
            .andExpect(jsonPath("$[0].items[0].name", is("Burger")))
            .andReturn();

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
            .andExpect(jsonPath("$").exists())              // order != null
            .andExpect(jsonPath("paid").value(false))       // paid == false
            .andExpect(jsonPath("items").doesNotExist())    // items == null
            .andReturn();
    }

    @Test
    public void addItemTest() throws Exception {
        // setup
        Order order = createEmptyOrder(1L);
        Item item = createBurgerItem(null);

        doNothing().when(orderService).addItem(order.getId(), item.getId());

        String path = "/order/" + order.getId() + "/addItem/" + item.getId();

        // execution and verification
        mockMvc.perform(put(path))
            .andExpect(status().isOk());
    }

    @Test
    public void payValidAmountTest() throws Exception {
        // setup
        Long orderId = 1L;
        BigDecimal payment = new BigDecimal("10.00");

        Order order = createBurgerOrder(orderId);

        Reciept reciept = createReciept(order, payment);
        reciept.getOrder().setPaid(true);

        when(orderService.pay(orderId, payment))
            .thenReturn(reciept);

        String path = "/order/" + order.getId() + "/pay";
        final MockHttpServletRequestBuilder PUT_REQUEST = put(path).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution and verification
        double change = reciept.getChange().doubleValue();
        double burgerPrice = order.getItems().get(0).getPrice().doubleValue();

        mockMvc.perform(PUT_REQUEST.content(payment.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payment", is(payment.doubleValue())))
            .andExpect(jsonPath("$.order.paid", is(true)))
            .andExpect(jsonPath("$.order.items[0].name", is("Burger")))
            .andExpect(jsonPath("$.order.items[0].price", is(burgerPrice)))
            .andExpect(jsonPath("$.change", is(change)));
    }

    @Test(expected = NestedServletException.class)
    public void payInvalidAmountTest() throws Exception {
        // setup
        Long orderId = 1L;
        BigDecimal payment = new BigDecimal("1.00");

        Order order = createBurgerOrder(orderId);

        InsufficientFundsException exception =
            new InsufficientFundsException("The payment must cover the cost of the order.");

        when(orderService.pay(orderId, payment)).thenThrow(exception);

        String path = "/order/" + order.getId() + "/pay";
        final MockHttpServletRequestBuilder PUT_REQUEST = put(path).contentType(JSON_UTF8_MEDIA_TYPE);

        // execution and verification
        mockMvc.perform(PUT_REQUEST.content(payment.toString()))
            .andExpect(status().isInternalServerError());
    }
}
