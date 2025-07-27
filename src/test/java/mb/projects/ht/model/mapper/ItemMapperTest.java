package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.ItemPriceDAO;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.enums.TimeFrameEnum;
import mb.projects.ht.model.Item;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemMapperTest {

    @Mock
    private ItemPriceMapper itemPriceMapper;

    @InjectMocks
    private ItemMapper itemMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void toItem_returnsNull_whenItemPriceDAOIsNull() {
        Item result = itemMapper.toItem(null, 1, 5);
        assertNull(result);
    }

    @Test
    public void toItem_mapsFieldsCorrectly() {
        // Arrange
        ItemPriceDAO itemPriceDAO = new ItemPriceDAO();
        // Assuming ItemPriceDAO has a setName method
        itemPriceDAO.setName("TestItem");

        Integer actionId = ActionEnum.ADD.getId();  // Assuming ADD is a valid enum constant
        Integer quantity = 3;
        RecurringPrice recurringPrice = new RecurringPrice();
        recurringPrice.setAmount(BigDecimal.valueOf(9.99));
        recurringPrice.setDuration(12L);
        recurringPrice.setTimeFrame(TimeFrameEnum.MONTH);

        Price mockedPrice = new Price();
        mockedPrice.setRecurringPrice(recurringPrice);

        when(itemPriceMapper.getPrice(itemPriceDAO)).thenReturn(mockedPrice);

        // Act
        Item item = itemMapper.toItem(itemPriceDAO, actionId, quantity);

        // Assert
        assertNotNull(item);
        assertEquals(mockedPrice, item.getItemPrice());
        assertEquals("TestItem", item.getIdentifier());
        assertEquals(ActionEnum.fromId(actionId).getName(), item.getAction());
        assertEquals(Long.valueOf(quantity), item.getQuantity());

        verify(itemPriceMapper, times(1)).getPrice(itemPriceDAO);
    }
}
