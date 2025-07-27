package mb.projects.ht.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mb.projects.ht.enums.TimeFrameEnum;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RecurringPrice {

    BigDecimal amount;
    Long duration;
    TimeFrameEnum timeFrame;
}
