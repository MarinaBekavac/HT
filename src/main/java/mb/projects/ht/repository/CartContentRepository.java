package mb.projects.ht.repository;

import mb.projects.ht.entities.CartContentDAO;
import mb.projects.ht.entities.CartDAO;
import mb.projects.ht.entities.ItemPriceDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartContentRepository extends JpaRepository<CartContentDAO, Long> {
    //boolean existsByCartAndItem(CartDAO cart, ItemDAO item);
    Optional<CartContentDAO> findByCartAndItemPrice(CartDAO cart, ItemPriceDAO itemPrice);

}
