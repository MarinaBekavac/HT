package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.ItemPriceDAO;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    @Autowired
    private ItemPriceMapper itemPriceMapper;


    public Item toItem(ItemPriceDAO itemPriceDAO, Integer actionId, Integer quantity) {
        if (itemPriceDAO == null) {
            return null;
        }

        Item item = new Item();
        // ... other field mappings

        item.setItemPrice(itemPriceMapper.getPrice(itemPriceDAO));
        item.setIdentifier(itemPriceDAO.getName());
        item.setAction(ActionEnum.fromId(actionId).getName());
        item.setQuantity(Long.valueOf(quantity));

        return item;
    }

}