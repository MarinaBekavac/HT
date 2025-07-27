package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.ItemDAO;
import mb.projects.ht.entities.PriceDAO;
import mb.projects.ht.entities.RecurringPriceDAO;
import mb.projects.ht.enums.TimeFrameEnum;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceMapper {

    public Price toPrice(PriceDAO priceDAO) {
        if (priceDAO == null) {
            return null;
        }

        Price price = new Price();
        price.setItemId(priceDAO.getItem().getId());
        price.setOneTimePrice(priceDAO.getOneTimePrice() != null ?
                priceDAO.getOneTimePrice().longValue() : null);

        if (priceDAO.getRecurringPriceList() != null) {
            price.setRecurringPrices(
                    priceDAO.getRecurringPriceList().stream()
                            .map(this::toRecurringPrice)
                            .collect(Collectors.toList())
            );
        }

            return price;
    }


    public PriceDAO toPriceDAO(Price price, ItemDAO itemDAO) {
        if (price == null) {
            return null;
        }

        PriceDAO priceDAO = new PriceDAO();
        priceDAO.setItem(itemDAO);
        priceDAO.setOneTimePrice(price.getOneTimePrice() != null ?
                BigDecimal.valueOf(price.getOneTimePrice()) : null);

        if (price.getRecurringPrices() != null) {
            List<RecurringPriceDAO> recurringPriceDAOs = price.getRecurringPrices().stream()
                    .map(rp -> toRecurringPriceDAO(rp, priceDAO))
                    .collect(Collectors.toList());
            priceDAO.setRecurringPriceList(recurringPriceDAOs);
        }

        return priceDAO;
    }

    private RecurringPrice toRecurringPrice(RecurringPriceDAO recurringPriceDAO) {
        if (recurringPriceDAO == null) {
            return null;
        }

        return new RecurringPrice(
                recurringPriceDAO.getAmount().longValue(),
                Long.valueOf(recurringPriceDAO.getDurationUnits()),
                TimeFrameEnum.values()[recurringPriceDAO.getTimeFrameId() - 1]
        );
    }

    private RecurringPriceDAO toRecurringPriceDAO(RecurringPrice recurringPrice, PriceDAO priceDAO) {
        if (recurringPrice == null) {
            return null;
        }

        RecurringPriceDAO recurringPriceDAO = new RecurringPriceDAO();
        recurringPriceDAO.setPrice(priceDAO);
        recurringPriceDAO.setAmount(BigDecimal.valueOf(recurringPrice.getAmount()));
        recurringPriceDAO.setDurationUnits(recurringPrice.getDuration().intValue());
        recurringPriceDAO.setTimeFrameId(recurringPrice.getTimeFrame().ordinal() + 1);

        return recurringPriceDAO;
    }
}
