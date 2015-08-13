package answer.king.controller;

import answer.king.model.Order;
import answer.king.model.Reciept;
import answer.king.service.OrderService;
import answer.king.throwables.exception.AnswerKingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Order getOrder(@PathVariable("id") Long id) {
        return orderService.getOrder(id);
    }

	@RequestMapping(method = RequestMethod.POST)
	public Order create() {
		Order order = new Order();
		return orderService.save(order);
	}

	@RequestMapping(value = "/{id}/addItem/{itemId}", method = RequestMethod.PUT)
	public void addItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, @RequestBody Integer qty)
        throws AnswerKingException {
		orderService.addItem(id, itemId, qty);
	}

	@RequestMapping(value = "/{id}/pay", method = RequestMethod.PUT)
	public Reciept pay(@PathVariable("id") Long id, @RequestBody BigDecimal payment) throws AnswerKingException {
		return orderService.pay(id, payment);
	}
}
