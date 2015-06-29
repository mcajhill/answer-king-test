package answer.king.service;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import answer.king.throwables.exception.InvalidItemException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    public void saveValidItemTest() throws Exception {
        // setup
        Item item = createBurgerItem(null);
        when(itemRepository.save(item)).thenReturn(item);

        // execution
        Item result = itemService.save(item);

        // verification
        assertNotNull(result);
        assertEquals(item, result);

        verify(itemRepository, times(1)).save(item);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test(expected = InvalidItemException.class)
    public void saveInvalidItemTest() throws Exception {
        // setup
        Item item = new Item();

        // execution
        itemService.save(item);

        // verification
        verify(itemRepository, times(1)).save(item);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void getAllTest() {
        // setup
        List<Item> items = createItemsList(null);
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
