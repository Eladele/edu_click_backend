package mr.master.edu_click.professeur.services;


import mr.master.edu_click.dao.entities.ProfesseurEntity;
import mr.master.edu_click.dao.repositories.ProfesseurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesseurService {

    private final ProfesseurRepository professeurRepository;

    public ProfesseurService(ProfesseurRepository professeurRepository) {
        this.professeurRepository = professeurRepository;
    }

    public List<ProfesseurEntity> findAll() {
        return professeurRepository.findAll();
    }

    public ProfesseurEntity save(ProfesseurEntity professeur) {
        return professeurRepository.save(professeur);
    }

    public ProfesseurEntity findByEmail(String email) {
        return professeurRepository.findByEmail(email);
    }
}
