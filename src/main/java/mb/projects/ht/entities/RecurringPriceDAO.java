package mb.projects.ht.entities;

import jakarta.persistence.*;
import lombok.*;
import mb.projects.ht.enums.TimeFrameEnum;

import java.math.BigDecimal;

@Entity
@Table(name = "recurring_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringPriceDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id", nullable = false)
    private PriceDAO price;

    @Column(name = "time_frame_id", nullable = false)
    private Integer timeFrameId;

    @Column(name = "duration_units", nullable = false)
    private Integer durationUnits;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}
