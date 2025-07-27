package mb.projects.ht.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mb.projects.ht.model.Price;
import mb.projects.ht.validator.ValidPrice;
import mb.projects.ht.validator.ValidUserIdOrTransactionId;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ValidPrice
@ValidUserIdOrTransactionId
public class AddItem {

    @NotBlank(message = "Source ID cannot be blank")
    String sourceId;
    @NotBlank(message = "Name cannot be blank")
    String name;
    @NotBlank(message = "Description cannot be blank")
    String description;
    Price price;
    String userId;
    String transactionId;
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Value must be greater than 0")
    Integer quantity;

    public boolean isRecurring() {
        if(price.getOneTimePrice()!=null) {
            return false;
        }
        return true;
    }

}
