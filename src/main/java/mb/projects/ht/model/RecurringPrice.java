package mb.projects.ht.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mb.projects.ht.enums.TimeFrameEnum;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RecurringPrice {

    Long amount;
    Long duration;
    TimeFrameEnum timeFrame;
}
