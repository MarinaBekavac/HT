package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.CartContentDAO;
import mb.projects.ht.entities.CartDAO;
import mb.projects.ht.entities.ItemPriceDAO;
import mb.projects.ht.enums.TimeFrameEnum;
import mb.projects.ht.model.Cart;
import mb.projects.ht.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartMapperTest {

    @InjectMocks
    private CartMapper cartMapper;

    @Mock
    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toCart_NullInput_ReturnsNull() {
        Cart result = cartMapper.toCart(null);
        assertNull(result);
    }

    @Test
    void toCart_EmptyContents_ReturnsCartWithEmptyItems() {
        CartDAO cartDAO = new CartDAO();
        cartDAO.setUserId("userID");
        cartDAO.setDateCreated(LocalDate.of(2023, 1, 1));
        cartDAO.setDateModified(LocalDate.of(2023, 1, 2));
        cartDAO.setTransactionId("tx123");
        cartDAO.setContents(Collections.emptyList());

        Cart cart = cartMapper.toCart(cartDAO);

        assertNotNull(cart);
        assertEquals("userID", cart.getUserId());
        assertEquals(
                Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                cart.getDateCreated()
        );

        assertEquals(
                Date.from(LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                cart.getDateModified()
        );

        assertEquals("tx123", cart.getTransactionId());
        assertTrue(cart.getListOfItemsInCart().isEmpty());
    }

    @Test
    void toCart_WithContents_MapsItems() {
        ItemPriceDAO itemPriceDAO1 = new ItemPriceDAO(1L, "sourceId1", "name", "description",
                BigDecimal.valueOf(100), null, null, null);
        CartContentDAO content1 = new CartContentDAO();
        content1.setItemPrice(itemPriceDAO1);
        content1.setActionId(1);
        content1.setQuantity(2);

        ItemPriceDAO itemPriceDAO2 = new ItemPriceDAO(1L, "sourceId1", "name", "description",
                null, Long.valueOf(TimeFrameEnum.MONTH.getId()), 1L, BigDecimal.valueOf(9.99));
        CartContentDAO content2 = new CartContentDAO();
        content2.setItemPrice(itemPriceDAO2);
        content2.setActionId(2);
        content2.setQuantity(3);

        List<CartContentDAO> contents = Arrays.asList(content1, content2);

        CartDAO cartDAO = new CartDAO();
        cartDAO.setUserId("456L");
        cartDAO.setDateCreated(LocalDate.of(2024, 5, 6));
        cartDAO.setDateModified(LocalDate.of(2024, 5, 7));
        cartDAO.setTransactionId("tx456");
        cartDAO.setContents(contents);

        Item item1 = new Item();
        Item item2 = new Item();

        when(itemMapper.toItem(itemPriceDAO1, 1, 2)).thenReturn(item1);
        when(itemMapper.toItem(itemPriceDAO2, 2, 3)).thenReturn(item2);

        Cart cart = cartMapper.toCart(cartDAO);

        assertNotNull(cart);
        assertEquals("456L", cart.getUserId());
        assertEquals(Date.from(LocalDate.of(2024, 5, 6).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), cart.getDateCreated());
        assertEquals(Date.from(LocalDate.of(2024, 5, 7).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), cart.getDateModified());
        assertEquals("tx456", cart.getTransactionId());

        List<Item> items = cart.getListOfItemsInCart();
        assertNotNull(items);
        assertEquals(2, items.size());
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));

        verify(itemMapper).toItem(itemPriceDAO1, 1, 2);
        verify(itemMapper).toItem(itemPriceDAO2, 2, 3);
    }
}
