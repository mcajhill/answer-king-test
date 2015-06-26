package answer.king.service;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;


    @Test
    public void saveTest() {
        // setup
        Item item = new Item();
        item.setId(1L);
        item.setName("Burger");
        item.setPrice(new BigDecimal("0.99"));

        when(itemRepository.save(item)).thenReturn(item);

        // execution
        Item result = itemService.save(item);

        // verification
        assertNotNull(result);
        assertEquals(item, result);

        verify(itemRepository, times(1)).save(item);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void findAllTest() {
        // setup
        List<Item> items = new ArrayList<>();
        items.add(new Item());

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
