package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.ItemPriceDAO;
import mb.projects.ht.enums.TimeFrameEnum;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import org.springframework.stereotype.Component;


@Component
public class ItemPriceMapper {

    public Price getPrice(ItemPriceDAO itemPriceDAO) {
        Price price = new Price();
        if(itemPriceDAO.getOneTimePrice()!=null) {
            price.setRecurringPrice(null);
            //price.setRecurringPrices(null);
            price.setOneTimePrice(itemPriceDAO.getOneTimePrice());
        } else {
            RecurringPrice recurringPrice = new RecurringPrice(itemPriceDAO.getAmount(), itemPriceDAO.getDurationUnits(), TimeFrameEnum.fromId(itemPriceDAO.getTimeFrameId()));
            price.setOneTimePrice(null);
            price.setRecurringPrice(recurringPrice);
            //price.setRecurringPrices(null);
        }

        return price;
    }

}
