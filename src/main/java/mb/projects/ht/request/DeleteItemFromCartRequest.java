package mb.projects.ht.request;

import jakarta.validation.constraints.NotBlank;
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
public class DeleteItemFromCartRequest {

    String userId;
    String transactionId;

    @NotBlank(message = "Source ID cannot be blank")
    String sourceId;

}
