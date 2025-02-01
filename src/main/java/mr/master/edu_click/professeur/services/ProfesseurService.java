package mr.master.edu_click.professeur.services;

import jakarta.persistence.EntityNotFoundException;
import mr.master.edu_click.dao.entities.ProfesseurEntity;
import mr.master.edu_click.dao.entities.UtilisateurEntity;
import mr.master.edu_click.dao.repositories.ProfesseurRepository;
import mr.master.edu_click.dao.repositories.UtilisateurRepository;
import mr.master.edu_click.professeur.dtos.ProfesseurDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesseurService {

    private final ProfesseurRepository professeurRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

//    public void UtilisateurService(UtilisateurRepository utilisateurRepository) {
//        this.utilisateurRepository = utilisateurRepository;
//    }
    public ProfesseurService(ProfesseurRepository professeurRepository) {
        this.professeurRepository = professeurRepository;
    }

    public List<ProfesseurDto> getAll() {
        return professeurRepository.findAll().stream()
                .map(ProfesseurEntity::toDto)
                .collect(Collectors.toList());
    }

    public ProfesseurDto getById(Long id) {
        return professeurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professeur introuvable avec l'ID : " + id))
                .toDto();
    }
    public Long add(ProfesseurDto professeurDto) {
        // Récupération de l'utilisateur associé
        UtilisateurEntity utilisateur = utilisateurRepository.findById(professeurDto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Conversion de ProfesseurDto en ProfesseurEntity
        ProfesseurEntity professeurEntity = professeurDto.toEntity();

        // Association de l'utilisateur à l'entité professeur
        professeurEntity.setUtilisateur(utilisateur);

        // Enregistrement de l'entité professeur
        return professeurRepository.save(professeurEntity).getId();
    }

//    public Long add(ProfesseurDto professeurDto) {
//        UtilisateurEntity utilisateur = utilisateurRepository.findById(professeurDto.getUtilisateurId())
//                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
//
//        // 2. Convertir ProfesseurDto en ProfesseurEntity
//        ProfesseurEntity professeurEntity = professeurDto.toEntity();
//
//        // 3. Associer l'utilisateur à l'entité professeur
//        professeurEntity.setUtilisateur(utilisateur);
//        return professeurRepository.save(professeurDto.toEntity()).getId();
//    }

    public Long update(ProfesseurDto professeurDto, Long id) {
        ProfesseurEntity professeurEntity = professeurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professeur introuvable avec l'ID : " + id));
        return professeurRepository.saveAndFlush(professeurDto.toEntity(professeurEntity)).getId();
    }

    public void deleteById(Long id) {
        professeurRepository.deleteById(id);
    }
}
