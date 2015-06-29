package answer.king.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import answer.king.throwables.exception.InvalidItemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;

@Service
@Transactional
public class ItemService {
	@Autowired
	private ItemRepository itemRepository;

	public List<Item> getAll() {
		List<Item> items = new ArrayList<>();
		for(Item item : itemRepository.findAll()) {
			items.add(item);
		}
		return items;
	}

	public Item save(Item item) throws InvalidItemException {
        String name = item.getName();
        BigDecimal price = item.getPrice();

        boolean invalidName = ( name == null || name.isEmpty() );
        boolean invalidPrice = ( price == null || price.compareTo(BigDecimal.ZERO) < 0 );

        if (invalidName || invalidPrice)
            throw new InvalidItemException("Item must have a valid name and price");

		return itemRepository.save(item);
	}
}
