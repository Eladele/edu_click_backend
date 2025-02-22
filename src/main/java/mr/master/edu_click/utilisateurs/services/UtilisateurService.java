package mr.master.edu_click.utilisateurs.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mr.master.edu_click.auth.JwtService;
import mr.master.edu_click.dao.entities.UtilisateurEntity;
import mr.master.edu_click.dao.repositories.UtilisateurRepository;
import mr.master.edu_click.utilisateurs.dtos.UtilisateurDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Trouver un utilisateur par ID
    public UtilisateurEntity findById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + id));
    }

    // Récupérer un utilisateur par ID en DTO
    public UtilisateurDto getById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + id))
                .toDto();
    }

    // Ajouter un utilisateur
    public Long add(UtilisateurDto utilisateurDto) {
        UtilisateurEntity utilisateur = utilisateurDto.toEntity();
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword())); // 🔹 Cryptage du mot de passe
        return utilisateurRepository.save(utilisateur).getId();
    }
    public List<UtilisateurDto> getAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurEntity::toDto)
                .collect(Collectors.toList());
    }
    // Mettre à jour un utilisateur
    public Long update(UtilisateurDto utilisateurDto, Long id) {
        UtilisateurEntity utilisateurEntity = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + id));
        return utilisateurRepository.saveAndFlush(utilisateurDto.toEntity(utilisateurEntity)).getId();
    }

    // Supprimer un utilisateur par ID
    public void deleteById(Long id) {
        utilisateurRepository.deleteById(id);
    }

    // Authentifier un utilisateur et retourner les tokens
    public Map<String, String> authenticateUser(String username, String password) {
        UtilisateurEntity user = utilisateurRepository.findByEmail(username);

        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec l'email : " + username);
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken
            );
        } else {
            throw new RuntimeException("Mot de passe invalide");
        }
    }

    // Ajouter un cookie pour la session utilisateur
    public void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // Expire in 1 day
        response.addCookie(cookie);
    }

    // Déconnecter l'utilisateur en supprimant les cookies
    public void logout(HttpServletResponse response) {
        deleteCookie(response, "access_token");
        deleteCookie(response, "refresh_token");
    }

    // Supprimer un cookie spécifique
    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    // Extraire un token à partir des cookies
    public String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public UtilisateurEntity findByEmail(String email) {
        // Trouver l'utilisateur par son email
        UtilisateurEntity user = utilisateurRepository.findByEmail(email);

        // Vérification si l'utilisateur est trouvé
        if (user == null) {
            throw new EntityNotFoundException("Utilisateur non trouvé");
        }

        // Vérification que le mot de passe de l'utilisateur n'est pas null
        if (user.getPassword() == null) {
            throw new IllegalStateException("Mot de passe manquant pour l'utilisateur");
        }

        return user;
    }

    // Récupérer tous les utilisateurs si le token est valide
    public Map<String, String> getUserRole(String token) {
        if (token != null && jwtService.isTokenValid(token)) {
            String role = String.valueOf(jwtService.getRoleFromToken(token));
            Map<String, String> response = new HashMap<>();
            response.put("role", role);
            return response;
        }
        throw new SecurityException("Accès non autorisé");
    }
}
