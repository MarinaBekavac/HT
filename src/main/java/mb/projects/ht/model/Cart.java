package mb.projects.ht.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Cart {
    String userId;
    Date dateCreated;
    Date dateModified;
    String transactionId;
    List<Item> listOfItemsInCart;

}
