package mb.projects.ht.entities.finished;

import jakarta.persistence.*;
import lombok.*;
import mb.projects.ht.entities.CartContentDAO;
import mb.projects.ht.entities.ItemPriceDAO;

@Entity
@Table(name = "finished_cart_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FinishedCartContentDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private FinishedCartDAO cart;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private ItemPriceDAO itemPrice;

    @Column(name = "action_id")
    private Integer actionId;

    @Column(name = "quantity")
    private Integer quantity;

    public FinishedCartContentDAO(CartContentDAO contentDAO, FinishedCartDAO parentCart) {
        this.cart = parentCart;
        this.itemPrice = contentDAO.getItemPrice() != null ? contentDAO.getItemPrice() : null;
        this.actionId = contentDAO.getActionId();
        this.quantity = contentDAO.getQuantity();
    }
}
