package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.ItemPriceDAO;
import mb.projects.ht.enums.TimeFrameEnum;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemPriceMapperTest {

    private ItemPriceMapper itemPriceMapper;

    @BeforeEach
    void setUp() {
        itemPriceMapper = new ItemPriceMapper();
    }

    @Test
    void testGetPrice_WithOneTimePrice() {
        ItemPriceDAO itemPriceDAO = new ItemPriceDAO();
        itemPriceDAO.setOneTimePrice(BigDecimal.valueOf(49.99));

        Price price = itemPriceMapper.getPrice(itemPriceDAO);

        assertNotNull(price);
        assertEquals(BigDecimal.valueOf(49.99), price.getOneTimePrice());
        assertNull(price.getRecurringPrice());
    }

    @Test
    void testGetPrice_WithRecurringPrice() {
        ItemPriceDAO itemPriceDAO = new ItemPriceDAO();
        itemPriceDAO.setOneTimePrice(null);
        itemPriceDAO.setAmount(BigDecimal.valueOf(19.99));
        itemPriceDAO.setDurationUnits(6L);
        itemPriceDAO.setTimeFrameId(Long.valueOf(TimeFrameEnum.MONTH.getId()));

        Price price = itemPriceMapper.getPrice(itemPriceDAO);

        assertNotNull(price);
        assertNull(price.getOneTimePrice());
        RecurringPrice recurringPrice = price.getRecurringPrice();
        assertNotNull(recurringPrice);
        assertEquals(BigDecimal.valueOf(19.99), recurringPrice.getAmount());
        assertEquals(6L, recurringPrice.getDuration());
        assertEquals(TimeFrameEnum.MONTH, recurringPrice.getTimeFrame());
    }
}
