package answer.king.controller;

import answer.king.model.Item;
import answer.king.service.ItemService;
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
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Item> getAll() {
		return itemService.getAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Item create(@RequestBody Item item) throws AnswerKingException {
		return itemService.save(item);
	}

    @RequestMapping(value = "/{id}/updatePrice", method = RequestMethod.PUT)
    public Item updatePrice(@PathVariable("id") Long id, @RequestBody BigDecimal price) throws AnswerKingException {
        return itemService.updatePrice(id, price);
    }
}
