package mr.master.edu_click.dao.repositories;

import mr.master.edu_click.dao.entities.UtilisateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<UtilisateurEntity, Long> {
    UtilisateurEntity findByEmail(String email);
}