package mr.master.edu_click.utilisateurs.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mr.master.edu_click.dao.entities.UtilisateurEntity;
import mr.master.edu_click.dao.repositories.UtilisateurRepository;
import mr.master.edu_click.utilisateurs.dtos.UtilisateurDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

//    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
//        this.utilisateurRepository = utilisateurRepository;
//    }
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
}
    public List<UtilisateurDto> getAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurEntity::toDto)
                .collect(Collectors.toList());
    }

    public UtilisateurEntity findById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + id));
    }
    public UtilisateurEntity findByEmail(String email) {
        UtilisateurEntity user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (user.getPassword() == null) {
            throw new IllegalStateException("Mot de passe manquant pour l'utilisateur");
        }

        return user;
    }
    public UtilisateurDto getById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + id))
                .toDto();
    }


    public Long add(UtilisateurDto utilisateurDto) {
        UtilisateurEntity utilisateur = utilisateurDto.toEntity();
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword())); // 🔹 Cryptage du mot de passe
        return utilisateurRepository.save(utilisateur).getId();
    }

//    public Long add(UtilisateurDto utilisateurDto) {
//        return utilisateurRepository.save(utilisateurDto.toEntity()).getId();
//    }
    public Long update(UtilisateurDto utilisateurDto, Long id) {
        UtilisateurEntity utilisateurEntity = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + id));
        return utilisateurRepository.saveAndFlush(utilisateurDto.toEntity(utilisateurEntity)).getId();
    }

    public void deleteById(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
