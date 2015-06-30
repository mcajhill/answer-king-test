package answer.king.service;


import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Reciept;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.RecieptRepository;
import answer.king.throwables.exception.InsufficientFundsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static answer.king.util.ModelUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RecieptRepository recieptRepository;


    @Before
    public void init() {
        Mockito.reset(orderRepository, itemRepository);
    }

    @Test
    public void getAllTest() {
        // setup
        List<Order> orders = createOrdersList();

        when(orderService.getAll()).thenReturn(orders);

        // execution
        List<Order> results = orderService.getAll();

        // verification
        assertNotNull(results);
        assertEquals(orders, results);

        verify(orderRepository, times(1)).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void saveTest() {
        // setup
        Order order = createEmptyOrder(1L);
        when(orderRepository.save(order)).thenReturn(order);

        // execution
        Order result = orderRepository.save(order);

        // verification
        assertNotNull(result);
        assertEquals(order, result);

        verify(orderRepository, times(1)).save(order);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void addItemTest() {
        // setup
        Long orderId = 1L;
        Long itemId = 1L;

        Order order = createEmptyOrder(orderId);
        Item item = createEmptyItem(itemId);

        when(orderRepository.findOne(orderId)).thenReturn(order);
        when(itemRepository.findOne(itemId)).thenReturn(item);

        // execution
        orderService.addItem(orderId, itemId);

        // verification
        assertEquals(item.getOrder(), order);
        assertTrue( order.getItems().contains(item) );

        verify(orderRepository, times(1)).findOne(orderId);
        verify(orderRepository, times(1)).save(order);
        verify(itemRepository, times(1)).findOne(itemId);

        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void payValidAmountTest() throws Exception {
        // setup
        Long orderId = 1L;
        BigDecimal payment = BigDecimal.TEN;

        Order order = createEmptyOrder(orderId);
        order.setItems(createItemsList(order));

        Reciept reciept = createReciept(order, payment);
        reciept.setId(1L);
        order.setReciept(reciept);

        when(orderRepository.findOne(orderId)).thenReturn(order);
        when(recieptRepository.save( any(Reciept.class) )).thenReturn(reciept);

        // execution
        Reciept result = orderService.pay(orderId, payment);
        BigDecimal change = result.getChange();
        Order orderResult = result.getOrder();

        // verification
        assertEquals(orderResult, order);
        assertEquals(result.getPayment(), payment);
        assertEquals(result.getId(), reciept.getId());
        assertEquals(orderResult.getPaid(), true);
        assertTrue(change.compareTo(BigDecimal.ZERO) >= 0);

        verify(orderRepository, times(1)).findOne(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test(expected = InsufficientFundsException.class)
    public void payInvalidAmountTest() throws InsufficientFundsException {
        // setup
        Long orderId = 1L;
        BigDecimal payment = BigDecimal.ZERO;

        Order order = createEmptyOrder(orderId);
        order.setItems(createItemsList(order));

        when(orderRepository.findOne(orderId)).thenReturn(order);

        // execution
        orderService.pay(orderId, payment);

        // verification
        assertEquals(order.getPaid(), false);

        verify(orderRepository, times(1)).findOne(orderId);
        verifyNoMoreInteractions(orderRepository);
    }
}
