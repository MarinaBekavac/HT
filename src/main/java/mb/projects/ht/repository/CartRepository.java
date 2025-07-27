package mb.projects.ht.repository;

import mb.projects.ht.entities.CartDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartDAO, Long> {

    @Query("SELECT c FROM CartDAO c LEFT JOIN FETCH c.contents WHERE c.userId = :userId")
    Optional<CartDAO> findByUserIdWithContents(@Param("userId") String userId);

    // Regular find by user ID without contents
    Optional<CartDAO> findByUserId(String userId);

    CartDAO getCartByUserId(String userId);

    @Query("SELECT DISTINCT c FROM CartDAO c " +
            "JOIN c.contents cc " +
            "JOIN cc.item i " +
            "WHERE i.sourceId = :sourceId " +
            "AND cc.actionId = :actionId")
    List<CartDAO> findCartsByItemSourceIdAndActionId(
            @Param("sourceId") String sourceId,
            @Param("actionId") Integer actionId);

    @Query("SELECT DISTINCT c FROM CartDAO c " +
            "JOIN c.contents cc " +
            "JOIN cc.item i " +
            "WHERE i.sourceId = :sourceId " +
            "AND cc.actionId = :actionId " +
            "AND c.dateCreated BETWEEN :dateFrom AND :dateTo")
    List<CartDAO> findCartsByItemAndActionInPeriod(
            @Param("sourceId") String sourceId,
            @Param("actionId") Integer actionId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);

    @Query("SELECT c FROM CartDAO c WHERE " +
            "(:userId IS NOT NULL AND c.userId = :userId) OR " +
            "(:transactionId IS NOT NULL AND c.transactionId = :transactionId)")
    Optional<CartDAO> findByUserIdOrTransactionId(
            @Param("userId") String userId,
            @Param("transactionId") String transactionId);


    @Query("SELECT DISTINCT c FROM CartDAO c JOIN c.contents cc JOIN cc.item i WHERE " +
            "(:userId IS NOT NULL AND c.userId = :userId) AND " +
            "(:sourceId IS NOT NULL AND i.sourceId = :sourceId)")
    Optional<CartDAO> findByUserIdAndItemSourceId(
            @Param("userId") String userId,
            @Param("sourceId") String sourceId);

//    @Modifying
//    @Query("DELETE FROM CartContentDAO cc WHERE " +
//            "cc.cart.id = :cartId AND " +
//            "cc.item.sourceId = :sourceId")
//    void deleteByCartIdAndSourceId(
//            @Param("cartId") Long cartId,
//            @Param("sourceId") String sourceId);
    @Modifying
    @Query(value = "DELETE FROM cart_content WHERE cart_id = :cartId AND item_id IN (SELECT id FROM item WHERE source_id = :sourceId)", nativeQuery = true)
    void deleteByCartIdAndSourceId(@Param("cartId") Long cartId, @Param("sourceId") String sourceId);


}
