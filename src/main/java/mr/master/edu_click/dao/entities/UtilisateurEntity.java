package mr.master.edu_click.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import mr.master.edu_click.utilisateurs.dtos.UtilisateurDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "utilisateurs")
public class UtilisateurEntity implements UserDetails {

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
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "est_active", nullable = false)
    private boolean estActive = true;

    @NotBlank(message = "Le numéro de téléphone ne peut pas être vide ou nul")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Le numéro de téléphone fourni est incorrect")
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfesseurEntity professeur;



    public enum Role {
        ADMIN,
        PROFESSEUR,
        ETUDIANT;


        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of();
        }
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public UtilisateurDto toDto() {
        return UtilisateurDto.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .role(this.role != null ? this.role.name() : null)
                .estActive(this.estActive)
                .telephone(this.telephone) // Pass the String directly
                .build();
    }
}

