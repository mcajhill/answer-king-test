package answer.king.util;

import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Reciept;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ModelUtil {

    public static Order createEmptyOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setItems(new ArrayList<Item>());
        return order;
    }

    public static Item createEmptyItem(Long itemId) {
        Item item = new Item();
        item.setId(itemId);
        return item;
    }

    public static Item createBurgerItem(Order order) {
        Item item = new Item();
        item.setId(1L);
        item.setName("Burger");
        item.setPrice(new BigDecimal("1.99"));
        item.setOrder(order);
        return item;
    }

    public static Item createChipsItem(Order order) {
        Item item = new Item();
        item.setId(2L);
        item.setName("Chips");
        item.setPrice(new BigDecimal("0.99"));
        item.setOrder(order);
        return item;
    }

    public static List<Order> createOrdersList() {
        List<Order> orders = new ArrayList<>();
        Order order = createEmptyOrder(1L);

        order.getItems().add(createBurgerItem(order));
        orders.add(order);

        return orders;
    }

    public static List<Item> createItemsList(Order order) {
        List<Item> items = new ArrayList<>();
        items.add(createBurgerItem(order));
        items.add(createChipsItem(order));
        return items;
    }

    public static Order createBurgerOrder(Long orderId) {
        Order order = createEmptyOrder(orderId);
        order.getItems().add(createBurgerItem(order));
        return order;
    }

    public static Reciept createReciept(Order order, BigDecimal payment) {
        Reciept reciept = new Reciept();
        reciept.setPayment(payment);
        reciept.setOrder(order);
        return reciept;
    }
}
