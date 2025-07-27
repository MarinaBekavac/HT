package mb.projects.ht.repository;

import mb.projects.ht.entities.CartDAO;
import mb.projects.ht.entities.finished.FinishedCartDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FinishedCartRepository extends JpaRepository<FinishedCartDAO, Long> {

    @Query("""
        SELECT DISTINCT c FROM FinishedCartDAO c
        JOIN c.contents cc
        JOIN cc.itemPrice i
        WHERE i.sourceId = :sourceId
          AND cc.actionId = :actionId
          AND c.dateCreated BETWEEN :dateFrom AND :dateTo
        """)
    List<FinishedCartDAO> findFinishedCartsByItemAndActionInPeriod(
            @Param("sourceId") String sourceId,
            @Param("actionId") Integer actionId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);

}
