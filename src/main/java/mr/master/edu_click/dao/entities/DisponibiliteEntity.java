package mr.master.edu_click.dao.entities;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DisponibiliteEntity {

    @NotBlank(message = "Le jour ne peut pas être vide ou nul")
    private String jour; // Exemple : "Lundi", "Mardi", ...

    @NotNull(message = "L'heure de début ne peut pas être nulle")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin ne peut pas être nulle")
    private LocalTime heureFin;
}
