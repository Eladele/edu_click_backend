package mr.master.edu_click.dao.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matieres")
public class MatiereEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Le nom de la matière ne peut pas être vide ou nul")
    @Column(name = "nom", nullable = false, unique = true) // Ajout de `unique = true` si chaque matière doit avoir un nom unique
    private String nom;

    @NotNull(message = "Le prix ne peut pas être nul")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à zéro")
    @Column(name = "prix", nullable = false)
    private Double prix;
}
