package mb.projects.ht.entities;

import jakarta.persistence.*;
import lombok.*;
import mb.projects.ht.model.RecurringPrice;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDAO item;

    @Column(name = "one_time_price", precision = 10, scale = 2)
    private BigDecimal oneTimePrice;

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurringPriceDAO> recurringPriceList;
    //private RecurringPriceDAO recurringPrice;

}
