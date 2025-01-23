package mr.master.edu_click.utilisateurs.services;


import mr.master.edu_click.dao.entities.UtilisateurEntity;
import mr.master.edu_click.dao.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<UtilisateurEntity> findAll() {
        return utilisateurRepository.findAll();
    }

    public UtilisateurEntity save(UtilisateurEntity utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public UtilisateurEntity findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
}
