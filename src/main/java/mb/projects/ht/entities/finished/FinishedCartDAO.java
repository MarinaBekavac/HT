package mb.projects.ht.entities.finished;

import jakarta.persistence.*;
import lombok.*;
import mb.projects.ht.entities.CartDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "finished_cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FinishedCartDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @Column(name = "date_bought")
    private LocalDate dateBought;

    @Column(name = "transaction_id")
    private String transactionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FinishedCartContentDAO> contents;

    public FinishedCartDAO(CartDAO cartDAO) {
        this.id = null; // Let JPA handle new ID
        this.userId = cartDAO.getUserId();
        this.dateCreated = cartDAO.getDateCreated();
        this.dateModified = cartDAO.getDateModified();
        this.dateBought = cartDAO.getDateBought();
        this.transactionId = cartDAO.getTransactionId();
        if (cartDAO.getContents() != null) {
            this.contents = cartDAO.getContents().stream()
                    .map(content -> new FinishedCartContentDAO(content, this))
                    .collect(Collectors.toList());
        }
    }
}
