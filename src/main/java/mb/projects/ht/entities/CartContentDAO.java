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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private ItemDAO item;

    @Column(name = "action_id")
    private Integer actionId;

    @Column(name = "quantity")
    private Integer quantity;
}