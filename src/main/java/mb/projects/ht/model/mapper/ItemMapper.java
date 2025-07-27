package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.ItemDAO;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    @Autowired
    private PriceMapper priceMapper;

    public Item toItem(ItemDAO itemDAO) {
        if (itemDAO == null) {
            return null;
        }

        Item item = new Item();
        // ... other field mappings

        if (itemDAO.getPrice() != null) {
            item.setPrice(priceMapper.toPrice(itemDAO.getPrice()));
        }
        item.setIdentifier(itemDAO.getName());

        return item;
    }

    public Item toItem(ItemDAO itemDAO, Integer actionId) {
        if (itemDAO == null) {
            return null;
        }

        Item item = new Item();
        // ... other field mappings

        if (itemDAO.getPrice() != null) {
            item.setPrice(priceMapper.toPrice(itemDAO.getPrice()));
        }
        item.setIdentifier(itemDAO.getName());
        item.setAction(ActionEnum.fromId(actionId).getName());

        return item;
    }

    public Item toItem(ItemDAO itemDAO, Integer actionId, Integer quantity) {
        if (itemDAO == null) {
            return null;
        }

        Item item = new Item();
        // ... other field mappings

        if (itemDAO.getPrice() != null) {
            item.setPrice(priceMapper.toPrice(itemDAO.getPrice()));
        }
        item.setIdentifier(itemDAO.getName());
        item.setAction(ActionEnum.fromId(actionId).getName());
        item.setQuantity(Long.valueOf(quantity));

        return item;
    }

    public ItemDAO toItemDAO(Item item) {
        if (item == null) {
            return null;
        }

        ItemDAO itemDAO = new ItemDAO();
        // ... other field mappings

        if (item.getPrice() != null) {
            itemDAO.setPrice(priceMapper.toPriceDAO(item.getPrice(), itemDAO));
        }

        return itemDAO;
    }
}