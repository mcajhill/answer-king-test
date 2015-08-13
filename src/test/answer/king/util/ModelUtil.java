package answer.king.util;

import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Reciept;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ModelUtil {

    public static Order createEmptyOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setItems(new ArrayList<LineItem>());
        return order;
    }

    public static Item createBurgerItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Burger");
        item.setPrice(new BigDecimal("1.99"));
        return item;
    }

    public static Item createChipsItem() {
        Item item = new Item();
        item.setId(2L);
        item.setName("Chips");
        item.setPrice(new BigDecimal("0.99"));
        return item;
    }

    public static LineItem createLineItem(Item item) {
        LineItem lineItem = new LineItem();

        lineItem.setId(item.getId());
        lineItem.setQuantity(1);
        lineItem.setPrice(item.getPrice());
        lineItem.setItem(item);

        return lineItem;
    }

    public static LineItem createBurgerLineItem() {
        return createLineItem(createBurgerItem());
    }

    public static LineItem createChipsLineItem() {
        return createLineItem(createChipsItem());
    }

    public static List<Order> createOrdersList() {
        List<Order> orders = new ArrayList<>();
        orders.add(createBurgerOrder(1L));
        orders.add(createChipsOrder(2L));
        return orders;
    }

    public static List<Item> createItemsList() {
        List<Item> items = new ArrayList<>();
        items.add(createBurgerItem());
        items.add(createChipsItem());
        return items;
    }

    public static List<LineItem> createLineItemList() {
        List<LineItem> items = new ArrayList<>();
        items.add(createBurgerLineItem());
        items.add(createChipsLineItem());
        return items;
    }

    public static Order createBurgerOrder(Long orderId) {
        Order order = createEmptyOrder(orderId);
        order.getItems().add(createBurgerLineItem());
        return order;
    }

    public static Order createChipsOrder(Long orderId) {
        Order order = createEmptyOrder(orderId);
        order.getItems().add(createChipsLineItem());
        return order;
    }

    public static Reciept createReciept(Long id, Order order, BigDecimal payment) {
        Reciept reciept = new Reciept();
        reciept.setId(id);
        reciept.setPayment(payment);
        reciept.setOrder(order);
        return reciept;
    }
}
