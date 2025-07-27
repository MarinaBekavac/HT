package mb.projects.ht.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Price {
    Long itemId;
    BigDecimal oneTimePrice;
    //List<RecurringPrice> recurringPrices;
    RecurringPrice recurringPrice;
}
