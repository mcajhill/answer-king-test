package answer.king.controller;

import answer.king.model.Order;
import answer.king.model.Reciept;
import answer.king.service.OrderService;
import answer.king.throwables.exception.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Order> getAll() {
		return orderService.getAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Order create() {
		Order order = new Order();
		return orderService.save(order);
	}

	@RequestMapping(value = "/{id}/addItem/{itemId}", method = RequestMethod.PUT)
	public void addItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
		orderService.addItem(id, itemId);
	}

	@RequestMapping(value = "/{id}/pay", method = RequestMethod.PUT)
	public Reciept pay(@PathVariable("id") Long id, @RequestBody BigDecimal payment) throws InsufficientFundsException {
		return orderService.pay(id, payment);
	}
}
