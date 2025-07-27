package mb.projects.ht.response;

import lombok.*;
import mb.projects.ht.model.Cart;
import mb.projects.ht.model.Item;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class GetCartResponse {

    List<Item> items;

    public GetCartResponse(Cart cart) {
        this.items = cart.getListOfItemsInCart();
    }

}
