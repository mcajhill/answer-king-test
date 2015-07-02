package answer.king.service;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import answer.king.throwables.exception.AnswerKingException;
import answer.king.throwables.exception.InvalidItemNameException;
import answer.king.throwables.exception.InvalidItemPriceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static answer.king.util.ModelUtil.createBurgerItem;
import static answer.king.util.ModelUtil.createItemsList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;


    @Before
    public void init() {
        reset(itemRepository);
    }

    @Test
    public void saveValidNameTest() throws Exception {
        // setup
        Item item = createBurgerItem();
        when(itemRepository.save(item)).thenReturn(item);

        // execution
        Item result = itemService.save(item);

        // verification
        assertNotNull(result);
        assertEquals(item, result);

        verify(itemRepository, times(1)).save(item);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test(expected = InvalidItemNameException.class)
    public void saveInvalidNameTest() throws AnswerKingException {
        // setup
        Item item = new Item();

        // execution
        itemService.save(item);

        // verification
        verify(itemRepository, times(1)).save(item);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void updateValidPriceTest() throws Exception {
        // setup
        Item item = createBurgerItem();

        final Long ITEM_ID = item.getId();
        final BigDecimal UPDATED_PRICE = new BigDecimal("4.99");

        when(itemRepository.findOne(ITEM_ID)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);

        // execution
        Item result = itemService.updatePrice(ITEM_ID, UPDATED_PRICE);

        // verification
        assertNotNull(result);
        assertEquals(UPDATED_PRICE, result.getPrice());

        verify(itemRepository, times(1)).findOne(ITEM_ID);
        verify(itemRepository, times(1)).save(item);
    }

    @Test(expected = InvalidItemPriceException.class)
    public void updateInvalidPriceTest() throws AnswerKingException {
        // setup
        Item item = createBurgerItem();

        final Long ITEM_ID = item.getId();
        final BigDecimal UPDATED_PRICE = BigDecimal.TEN.negate();

        when(itemRepository.findOne(ITEM_ID)).thenReturn(item);

        // execution
        itemService.updatePrice(ITEM_ID, UPDATED_PRICE);

        // verification
        verify(itemRepository, times(1)).findOne(ITEM_ID);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void getAllTest() {
        // setup
        List<Item> items = createItemsList();
        when(itemRepository.findAll()).thenReturn(items);

        // execution
        List<Item> results = itemService.getAll();

        // verification
        assertNotNull(results);
        assertEquals(items, results);

        verify(itemRepository, times(1)).findAll();
        verifyNoMoreInteractions(itemRepository);
    }
}
