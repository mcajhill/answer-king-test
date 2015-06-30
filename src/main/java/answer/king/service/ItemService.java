package answer.king.service;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import answer.king.throwables.exception.AnswerKingException;
import answer.king.throwables.exception.InvalidItemNameException;
import answer.king.throwables.exception.InvalidItemPriceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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

	public Item save(Item item) throws AnswerKingException {
        preCommitValidation(item);
		return itemRepository.save(item);
	}

    public Item updatePrice(Long id, BigDecimal price) throws AnswerKingException {
        Item item = itemRepository.findOne(id);
        item.setPrice(price);
        return save(item);
    }

    private void preCommitValidation(Item item) throws AnswerKingException {
        String name = item.getName();
        BigDecimal price = item.getPrice();

        boolean invalidName = StringUtils.isEmpty(name);
        boolean invalidPrice = ( price == null || price.compareTo(BigDecimal.ZERO) < 0 );

        if (invalidName)
            throw new InvalidItemNameException();

        if (invalidPrice)
            throw new InvalidItemPriceException();
    }
}
