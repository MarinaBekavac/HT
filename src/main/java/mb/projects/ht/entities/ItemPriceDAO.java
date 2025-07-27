package mb.projects.ht.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "item_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@OneToOne
//    @JoinColumn(name = "item_id", nullable = false)
//    private String item;

    @Column(name = "source_id", nullable = false)
    private String sourceId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "one_time_price", precision = 10, scale = 2)
    private BigDecimal oneTimePrice;

    @Column(name = "time_frame_id")
    private Long timeFrameId;

    @Column(name = "duration_units")
    private Long durationUnits;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    public boolean isRecurringPrice() {
        return timeFrameId != null && durationUnits != null && amount != null && oneTimePrice == null;
    }

}
