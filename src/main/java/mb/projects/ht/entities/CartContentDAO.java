package mb.projects.ht.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartContentDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private CartDAO cart;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private ItemPriceDAO itemPrice;

    @Column(name = "action_id")
    private Integer actionId;

    @Column(name = "quantity")
    private Integer quantity;

    public CartContentDAO(Integer quantity, Integer actionId, ItemPriceDAO itemPrice, CartDAO cart) {
        this.quantity = quantity;
        this.actionId = actionId;
        this.itemPrice = itemPrice;
        this.cart = cart;
    }
}