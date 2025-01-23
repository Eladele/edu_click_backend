package mr.master.edu_click.dao.repositories;

import mr.master.edu_click.dao.entities.ProfesseurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesseurRepository extends JpaRepository<ProfesseurEntity, Long> {
    ProfesseurEntity findByEmail(String email);
}