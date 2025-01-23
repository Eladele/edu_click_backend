package mr.master.edu_click.dao.repositories;


import mr.master.edu_click.dao.entities.MatiereEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatiereRepository extends JpaRepository<MatiereEntity, Long> {
    MatiereEntity findByNom(String nom); // Requête personnalisée pour chercher une matière par son nom
}
