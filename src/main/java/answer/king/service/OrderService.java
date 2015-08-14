package answer.king.service;

import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Reciept;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.RecieptRepository;
import answer.king.throwables.exception.AnswerKingException;
import answer.king.throwables.exception.InsufficientFundsException;
import answer.king.throwables.exception.ItemDoesNotExistException;
import answer.king.throwables.exception.OrderAlreadyPaidException;
import answer.king.throwables.exception.OrderDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ItemRepository itemRepository;

    @Autowired
    private RecieptRepository recieptRepository;


	public List<Order> getAll() {
		List<Order> orders = new ArrayList<>();
		for(Order order : orderRepository.findAll()) {
			orders.add(order);
		}
		return orders;
	}

    public Order getOrder(Long id) {
        return orderRepository.findOne(id);
    }

	public Order save(Order order) {
		return orderRepository.save(order);
	}

	public void addItem(Long id, Long itemId, Integer qty) throws ItemDoesNotExistException {
		Order order = orderRepository.findOne(id);
		Item item = itemRepository.findOne(itemId);

        if (item == null)
            throw new ItemDoesNotExistException();

        LineItem lineItem = order.findLineItem(item);

        if (lineItem == null)
            lineItem = new LineItem();

        List<LineItem> items = order.getItems();

        if (items.contains(lineItem)) {
            Integer newQty = lineItem.getQuantity() + qty;
            lineItem.setQuantity(newQty);
        }
        else {
            lineItem.setName(item.getName());
            lineItem.setPrice(item.getPrice());
            lineItem.setQuantity(qty);
            lineItem.setItem(item);
            items.add(lineItem);
        }

        orderRepository.save(order);
	}

    public Reciept pay(Long id, BigDecimal payment) throws AnswerKingException {
		Order order = orderRepository.findOne(id);

        validateOrderStatus(order);

		Reciept reciept = new Reciept();
		reciept.setPayment(payment);
		reciept.setOrder(order);

        boolean invalidPayment = reciept.getChange().compareTo(BigDecimal.ZERO) < 0;

        if (invalidPayment)
            throw new InsufficientFundsException();

        order.setReciept(reciept);
        order.setPaid(true);

        return recieptRepository.save(reciept);
	}

    private void validateOrderStatus(Order order) throws AnswerKingException {
        if (order == null)
            throw new OrderDoesNotExistException();

        if (order.getReciept() != null)
            throw new OrderAlreadyPaidException();
    }
}
