package mb.projects.ht.repository;

public interface ItemRepository /*extends JpaRepository<ItemDAO, Long>*/ {

//    Optional<ItemDAO> findBySourceId(String sourceId);
//
//    @Query(value = """
//    SELECT i.*
//    FROM item i
//    LEFT JOIN price p ON p.item_id = i.id
//    LEFT JOIN recurring_price rp ON rp.price_id = p.id
//    WHERE i.source_id = :sourceId
//      AND (
//        (p.one_time_price IS NOT NULL AND p.one_time_price = :oneTimePrice)
//        OR EXISTS (
//            SELECT 1 FROM recurring_price rp2
//            WHERE rp2.price_id = p.id
//              AND rp2.amount = :recurringAmount
//              AND rp2.duration_units = :recurringDuration
//              AND rp2.time_frame_id = :timeFrameId
//        )
//    )
//""", nativeQuery = true)
//    List<ItemDAO> findItemWithMatchingPrice(
//            @Param("sourceId") String sourceId,
//            @Param("oneTimePrice") Long oneTimePrice,
//            @Param("recurringAmount") Long recurringAmount,
//            @Param("recurringDuration") Long recurringDuration,
//            @Param("timeFrameId") Long timeFrameId
//    );
//
//    @Query(value = """
//    SELECT i.*
//    FROM item i
//    LEFT JOIN item_price p ON p.item_id = i.id
//    WHERE i.source_id = :sourceId
//     AND p.one_time_price IS NOT NULL
//     AND p.one_time_price = :oneTimePrice
//""", nativeQuery = true)
//    ItemDAO findItemWithMatchingOneTimeItemPrice(
//            @Param("sourceId") String sourceId,
//            @Param("oneTimePrice") Long oneTimePrice
//    );
//
//    @Query(value = """
//    SELECT i.*
//    FROM item i
//    LEFT JOIN item_price p ON p.item_id = i.id
//    WHERE i.source_id = :sourceId
//    AND p.amount = :recurringAmount
//          AND p.duration_units = :recurringDuration
//          AND p.time_frame_id = :timeFrameId
//""", nativeQuery = true)
//    ItemDAO findItemWithMatchingRecurringItemPrice(
//            @Param("sourceId") String sourceId,
//            @Param("recurringAmount") Long recurringAmount,
//            @Param("recurringDuration") Long recurringDuration,
//            @Param("timeFrameId") Long timeFrameId
//    );


}
