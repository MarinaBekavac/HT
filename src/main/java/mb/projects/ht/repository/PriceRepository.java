package mb.projects.ht.repository;

import mb.projects.ht.entities.CartContentDAO;
import mb.projects.ht.entities.CartDAO;
import mb.projects.ht.entities.ItemDAO;
import mb.projects.ht.entities.PriceDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<PriceDAO, Long> {

}
