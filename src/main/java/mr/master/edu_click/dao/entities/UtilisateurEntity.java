package mr.master.edu_click.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "utilisateurs")
public class UtilisateurEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Email(message = "L'email fourni est incorrect")
    @NotBlank(message = "L'email ne peut pas être vide ou nul")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide ou nul")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role; // Utilisation d'un enum pour limiter les rôles possibles.

    @Column(name = "est_active", nullable = false)
    private boolean estActive = true;

    @NotBlank(message = "Le numéro de téléphone ne peut pas être vide ou nul")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Le numéro de téléphone fourni est incorrect")
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfesseurEntity professeur;

    // Enum pour définir les rôles possibles
    public enum Role {
        ADMIN,
        PROFESSEUR,
        ETUDIANT
    }
}
