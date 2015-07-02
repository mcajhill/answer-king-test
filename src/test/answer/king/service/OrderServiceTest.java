package answer.king.service;


import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Reciept;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.RecieptRepository;
import answer.king.throwables.exception.*;
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

    private final Long ORDER_ID = 1L;
    private final Long ITEM_ID = 1L;

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
    public void addLineItemFromExistingItemTest() throws AnswerKingException {
        // setup
        Order order = createEmptyOrder(ORDER_ID);
        Item item = createBurgerItem();

        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);
        when(itemRepository.findOne(ITEM_ID)).thenReturn(item);

        // execution
        orderService.addItem(ORDER_ID, ITEM_ID);

        // verification
        LineItem lineItem = order.getItems().get(0);

        assertEquals(item, lineItem.getItem());
        assertEquals(item.getPrice(), lineItem.getPrice());
        assertEquals(1, lineItem.getQuantity());

        verify(orderRepository, times(1)).findOne(ORDER_ID);
        verify(orderRepository, times(1)).save(order);
        verify(itemRepository, times(1)).findOne(ITEM_ID);

        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test(expected = ItemDoesNotExistException.class)
    public void addLineItemFromNonExistentItemTest() throws AnswerKingException {
        // setup
        Order order = createEmptyOrder(ORDER_ID);

        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);
        when(itemRepository.findOne(ITEM_ID)).thenReturn(null);

        // execution
        orderService.addItem(ORDER_ID, ITEM_ID);

        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void payValidAmountTest() throws AnswerKingException {
        // setup
        BigDecimal payment = BigDecimal.TEN;

        Order order = createEmptyOrder(ORDER_ID);
        order.setItems(createLineItemList());

        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);

        // execution
        orderService.pay(ORDER_ID, payment);
        Reciept result = order.getReciept();

        BigDecimal change = result.getChange();
        Order orderResult = result.getOrder();

        // verification
        assertEquals(order, orderResult);
        assertEquals(true, orderResult.getPaid());
        assertEquals(payment, result.getPayment());
        assertTrue(change.compareTo(BigDecimal.ZERO) >= 0);

        verify(orderRepository, times(1)).findOne(ORDER_ID);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test(expected = InsufficientFundsException.class)
    public void payInvalidAmountTest() throws AnswerKingException {
        // setup
        Order order = createBurgerOrder(ITEM_ID);
        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);

        // execution
        orderService.pay(ORDER_ID, BigDecimal.ZERO);

        // verification
        assertEquals(false, order.getPaid());

        verify(orderRepository, times(1)).findOne(ORDER_ID);
        verifyZeroInteractions(recieptRepository);
    }

    @Test(expected = OrderDoesNotExistException.class)
    public void payOrderNotExistsTest() throws AnswerKingException {
        // setup
        when(orderRepository.findOne(ORDER_ID)).thenReturn(null);

        // execution
        orderService.pay(ORDER_ID, BigDecimal.TEN);

        // verification
        verify(orderRepository, times(1)).findOne(ORDER_ID);
        verifyZeroInteractions(recieptRepository);
    }

    @Test(expected = OrderAlreadyPaidException.class)
    public void payTwiceTest() throws AnswerKingException {
        // setup
        BigDecimal payment = BigDecimal.ZERO;

        Order order = createBurgerOrder(ITEM_ID);
        Reciept reciept = createReciept(1L, order, payment);
        order.setReciept(reciept);

        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);

        // execution
        orderService.pay(ORDER_ID, payment);

        // verification
        verify(orderRepository, times(1)).findOne(ORDER_ID);
        verifyZeroInteractions(recieptRepository);
    }
}
