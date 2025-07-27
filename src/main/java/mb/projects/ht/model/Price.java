package mb.projects.ht.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Price {
    Long itemId;
    Long oneTimePrice;
    List<RecurringPrice> recurringPrices;
    //RecurringPrice recurringPrices;
}
