package mb.projects.ht.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mb.projects.ht.validator.ValidUserIdOrTransactionId;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ValidUserIdOrTransactionId
public class EvictCartRequest {

    String userId;
    String transactionId;

}
