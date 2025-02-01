package mr.master.edu_click.professeur.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.master.edu_click.dao.entities.DisponibiliteEntity;
import mr.master.edu_click.dao.entities.MatiereEntity;
import mr.master.edu_click.dao.entities.ProfesseurEntity;

import java.util.List;

/**
 * DTO pour la classe ProfesseurEntity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfesseurDto {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String adresse;
    private String ville;
    private String devise;
    private Double evaluation;
    private String genre;
    private List<String> numerosTelephone;
    private String biographie;
    private String introduction;
    private List<String> lieuxEnseignement;
    private List<String> langues;
    private List< String> education;
    private List<MatiereEntity> matieres;
    private List<DisponibiliteEntity> disponibilites;
//    private UtilisateurEntity utilisateur;
    private Long utilisateurId;

    public ProfesseurEntity toEntity() {
        return _toEntity(new ProfesseurEntity());
    }

    public ProfesseurEntity toEntity(ProfesseurEntity professeurEntity) {
        return _toEntity(professeurEntity);
    }

//    private ProfesseurEntity _toEntity(ProfesseurEntity professeurEntity) {
//        professeurEntity.setNom(nom);
//        professeurEntity.setPrenom(prenom);
//        professeurEntity.setEmail(email);
//        professeurEntity.setAdresse(adresse);
//        professeurEntity.setVille(ville);
//        professeurEntity.setDevise(devise);
//        professeurEntity.setEvaluation(evaluation);
//        professeurEntity.setGenre(genre);
//        return professeurEntity;
//    }
private ProfesseurEntity _toEntity(ProfesseurEntity professeurEntity) {
    return professeurEntity.toBuilder()
            .nom(nom)
            .prenom(prenom)
            .email(email)
            .adresse(adresse)
            .ville(ville)
            .devise(devise)
            .evaluation(evaluation)
            .genre(genre)
            .numerosTelephone(numerosTelephone != null ? List.copyOf(numerosTelephone) : null)
            .lieuxEnseignement(lieuxEnseignement != null ? List.copyOf(lieuxEnseignement) : null)
            .langues(langues != null ? List.copyOf(langues) : null)
            .education(education != null ? List.copyOf(education) : null)
            .matieres(matieres != null ? List.copyOf(matieres) : null)
            .disponibilites(disponibilites != null ? List.copyOf(disponibilites) : null)
//            .utilisateur(utilisateurId != null ? new UtilisateurEntity(utilisateurId) : null)
            .biographie(biographie)
            .introduction(introduction)
            .build();
}

}
