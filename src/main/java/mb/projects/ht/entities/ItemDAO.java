package mb.projects.ht.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mb.projects.ht.enums.PricingTypeEnum;
import mb.projects.ht.model.RecurringPrice;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false)
    private String sourceId;

    @Column(nullable = false)
    private String name;

    private String description;
//
//    @Column(name = "stock_quantity", columnDefinition = "INT DEFAULT 0")
//    private Integer stockQuantity;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private PriceDAO price;

    // Helper methods for price access
    public BigDecimal getOneTimePrice() {
        return price != null ? price.getOneTimePrice() : null;
    }

    public List<RecurringPriceDAO> getRecurringPriceList() {
        return price != null ? price.getRecurringPriceList() : Collections.emptyList();
    }

    // Convenience method to check if item is subscription
    public boolean isSubscription() {
        return price != null &&
                (price.getOneTimePrice() == null || price.getOneTimePrice().compareTo(BigDecimal.ZERO) == 0) &&
                !price.getRecurringPriceList().isEmpty();
    }

    // Convenience method to check if item is one-time purchase
    public boolean isOneTimePurchase() {
        return price != null &&
                price.getOneTimePrice() != null &&
                price.getOneTimePrice().compareTo(BigDecimal.ZERO) > 0 &&
                price.getRecurringPriceList().isEmpty();
    }
}
