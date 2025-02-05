package mr.master.edu_click.utilisateurs.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.master.edu_click.dao.entities.UtilisateurEntity;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDto {
    private Long id;
    private String email;
    private String password;
    private String role;
    private boolean estActive;
    private String telephone;

    public UtilisateurEntity toEntity() {
        return _toEntity(new UtilisateurEntity());
    }

    public UtilisateurEntity toEntity(UtilisateurEntity utilisateurEntity) {
        return _toEntity(utilisateurEntity);
    }

    private UtilisateurEntity _toEntity(UtilisateurEntity utilisateurEntity) {
        return utilisateurEntity.toBuilder()
                .email(email)
                .password(password != null ? password : "") // ✅ Évite que le password soit null
                .role(role != null ? UtilisateurEntity.Role.valueOf(role) : null)
                .estActive(estActive)
                .telephone(telephone)
                .build();
    }

}
