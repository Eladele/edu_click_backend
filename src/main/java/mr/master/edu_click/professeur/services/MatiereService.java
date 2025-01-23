package mr.master.edu_click.professeur.services;




import mr.master.edu_click.dao.entities.MatiereEntity;
import mr.master.edu_click.dao.repositories.MatiereRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatiereService {

    private final MatiereRepository matiereRepository;

    public MatiereService(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    public List<MatiereEntity> findAll() {
        return matiereRepository.findAll();
    }

    public MatiereEntity save(MatiereEntity matiere) {
        return matiereRepository.save(matiere);
    }

    public MatiereEntity findByNom(String nom) {
        return matiereRepository.findByNom(nom);
    }
}
