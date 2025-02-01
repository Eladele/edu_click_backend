package mr.master.edu_click.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import mr.master.edu_click.professeur.dtos.ProfesseurDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "professeurs")
public class ProfesseurEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide ou nul")
    @Size(min = 3, message = "Le nom doit contenir au moins 3 caractères")
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide ou nul")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Email(message = "L'email fourni est incorrect")
    @NotBlank(message = "L'email ne peut pas être vide ou nul")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @ElementCollection
    @Size(max = 3, message = "Vous pouvez ajouter jusqu'à trois numéros de téléphone")
    @CollectionTable(name = "professeur_telephones", joinColumns = @JoinColumn(name = "professeur_id"))
    @Column(name = "numero_telephone", nullable = false)
    private List<String> numerosTelephone;

    @Column(name = "biographie", length = 1000) // Limite de taille pour éviter de surcharger la base de données
    private String biographie;

//    @NotBlank(message = "L'introduction ne peut pas être vide ou nulle")
    @Column(name = "introduction", nullable = true, length = 500) // Limite de taille pour l'introduction
    private String introduction="Aucune introduction fournie";

    @ElementCollection
    @CollectionTable(name = "professeur_educations", joinColumns = @JoinColumn(name = "professeur_id"))
    @Column(name = "education")
    private List<@NotBlank(message = "L'éducation ne peut pas être vide ou nulle") String> education;

    @NotBlank(message = "L'adresse ne peut pas être vide ou nulle")
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "ville")
    private String ville;

    @Column(name = "devise")
    private String devise;

    @Column(name = "evaluation")
    private Double evaluation;

    @Column(name = "genre", length = 10) // Par exemple : "Homme" ou "Femme"
    private String genre;

    @ManyToMany
    @JoinTable(
            name = "professeur_matieres",
            joinColumns = @JoinColumn(name = "professeur_id"),
            inverseJoinColumns = @JoinColumn(name = "matiere_id")
    )
    private List<MatiereEntity> matieres;

    @ElementCollection
    @CollectionTable(name = "disponibilites", joinColumns = @JoinColumn(name = "professeur_id"))
    @Column(name = "disponibilite") // Par exemple, une chaîne : "Lundi 9h-11h"
    private List<DisponibiliteEntity> disponibilites;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", unique = true, nullable = false)
    private UtilisateurEntity utilisateur;

    @ElementCollection
    @CollectionTable(name = "professeur_langues", joinColumns = @JoinColumn(name = "professeur_id"))
    @Column(name = "langue")
    private List<String> langues;

    @ElementCollection
    @CollectionTable(name = "lieux_enseignement", joinColumns = @JoinColumn(name = "professeur_id"))
    @Column(name = "lieu_enseignement")
        private List<String> lieuxEnseignement;

    public ProfesseurDto toDto() {
        return mr.master.edu_click.professeur.dtos.ProfesseurDto.builder()
                .id(this.id)
                .nom(this.nom)
                .prenom(this.prenom)
                .email(this.email)
                .numerosTelephone(this.numerosTelephone)
                .biographie(this.biographie)
                .introduction(this.introduction)
                .education(this.education)
                .adresse(this.adresse)
                .ville(this.ville)
                .devise(this.devise)
                .evaluation(this.evaluation)
                .genre(this.genre)
                .matieres(this.matieres)
                .disponibilites(this.disponibilites)
                .utilisateurId(this.utilisateur != null ? this.utilisateur.getId() : null)
                .langues(this.langues)
                .lieuxEnseignement(this.lieuxEnseignement)
                .build();
    }

}
