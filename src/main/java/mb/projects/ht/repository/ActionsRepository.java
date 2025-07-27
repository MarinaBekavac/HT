package mb.projects.ht.repository;

import mb.projects.ht.entities.ActionsDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionsRepository extends JpaRepository<ActionsDAO, Long> {
    ActionsDAO findByActionId(Integer actionId);

}
