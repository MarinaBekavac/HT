package mb.projects.ht.repository;

import mb.projects.ht.entities.ItemPriceDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ItemPriceRepository extends JpaRepository<ItemPriceDAO, Long>  {

    @Query(value = """
    SELECT p.*
    FROM item_price p
    WHERE p.source_id = :sourceId
    AND p.one_time_price IS NOT NULL
    AND p.one_time_price = :oneTimePrice
""", nativeQuery = true)
    ItemPriceDAO findItemWithMatchingOneTimeItemPrice(
            @Param("sourceId") String sourceId,
            @Param("oneTimePrice") BigDecimal oneTimePrice
    );

    @Query(value = """
    SELECT p.*
    FROM item_price p
    WHERE p.source_id = :sourceId
    AND p.amount = :recurringAmount
    AND p.duration_units = :recurringDuration
    AND p.time_frame_id = :timeFrameId
""", nativeQuery = true)
    ItemPriceDAO findItemWithMatchingRecurringItemPrice(
            @Param("sourceId") String sourceId,
            @Param("recurringAmount") BigDecimal recurringAmount,
            @Param("recurringDuration") Long recurringDuration,
            @Param("timeFrameId") Long timeFrameId
    );

    List<ItemPriceDAO> findBySourceId(String sourceId);

}
