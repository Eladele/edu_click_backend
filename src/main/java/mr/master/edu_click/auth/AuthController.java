package mr.master.edu_click.auth;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mr.master.edu_click.auth.dtos.AuthResponse;
import mr.master.edu_click.dao.entities.UtilisateurEntity;
import mr.master.edu_click.utilisateurs.services.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import mr.master.edu_click.auth.dtos.LoginRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurService userService;
    private final JwtService jwtService;
    private final RedisTokenStore tokenStore;
    private final PasswordEncoder passwordEncoder;

//    @PostMapping("/login")
//    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
//        if (request.password() == null || request.password().isBlank()) {
//            return ResponseEntity.badRequest().build();
//        }
//        try {
//            UtilisateurEntity user = userService.findByEmail(request.email());
//
//            if (passwordEncoder.matches(request.password(), user.getPassword())) {
//                String accessToken = jwtService.generateToken(user);
//                String refreshToken = jwtService.generateRefreshToken(user);
//
//                // Stockage dans Redis
//                tokenStore.storeToken(user.getEmail(), accessToken, Duration.ofMillis(jwtService.getExpiration()));
//                tokenStore.storeRefreshToken(user.getEmail(), refreshToken, Duration.ofMillis(jwtService.getRefreshExpiration()));
//
//                // Configuration des cookies
//                setCookie(response, "access_token", accessToken, jwtService.getExpiration() / 1000);
//                setCookie(response, "refresh_token", refreshToken, jwtService.getRefreshExpiration() / 1000);
//
//                return ResponseEntity.ok().build();
//            }
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        } catch (EntityNotFoundException e) {
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    System.out.println("Email reçu: " + request.email());
    System.out.println("Mot de passe reçu: " + request.password());

    try {
        UtilisateurEntity user = userService.findByEmail(request.email());
        System.out.println("Utilisateur trouvé: " + user.getEmail());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            System.out.println("Mot de passe incorrect pour l'utilisateur: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects.");
        }

        // Générer les tokens JWT
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Stocker dans Redis
        tokenStore.storeToken(user.getEmail(), accessToken, Duration.ofMillis(jwtService.getExpiration()));
        tokenStore.storeRefreshToken(user.getEmail(), refreshToken, Duration.ofMillis(jwtService.getRefreshExpiration()));

        // Configurer les cookies
        setCookie(response, "access_token", accessToken, jwtService.getExpiration() / 1000);
        setCookie(response, "refresh_token", refreshToken, jwtService.getRefreshExpiration() / 1000);

        return ResponseEntity.ok(new AuthResponse(accessToken));

    } catch (EntityNotFoundException e) {
        System.out.println("Utilisateur non trouvé avec l'email: " + request.email());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé.");
    }
}
    // Méthode utilitaire pour configurer les cookies
    private void setCookie(HttpServletResponse response, String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ⚠️ Mettre `true` en production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge);
        response.addCookie(cookie);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue);

        if (refreshToken.isPresent() && jwtService.validateToken(refreshToken.get())) {
            String username = jwtService.extractUsername(refreshToken.get());

            if (tokenStore.validateRefreshToken(username, refreshToken.get())) {
                String newAccessToken = jwtService.generateToken(userService.loadUserByUsername(username));
                tokenStore.storeToken(username, newAccessToken, Duration.ofMillis(jwtService.getExpiration()));

                setCookie(response, "access_token", newAccessToken, jwtService.getExpiration() / 1000);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

//    private void setCookie(HttpServletResponse response, String name, String value, long maxAge) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // En production
//        cookie.setPath("/");
//        cookie.setMaxAge((int) maxAge);
//        response.addCookie(cookie);
//    }



//private void setCookie(HttpServletResponse response, String name, String value, long maxAge) {
//    Cookie cookie = new Cookie(name, value);
//    cookie.setHttpOnly(true);
//    cookie.setSecure(false); // Désactivé pour le développement en HTTP
//    cookie.setPath("/");
//    cookie.setMaxAge((int) maxAge);
//    response.addCookie(cookie);
//}

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Arrays.stream(request.getCookies())
                .filter(c -> "access_token".equals(c.getName()))
                .findFirst()
                .ifPresent(c -> {
                    String username = jwtService.extractUsername(c.getValue());
                    tokenStore.invalidateToken(username);
                });

        return ResponseEntity.ok().build();
    }


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
//        if (request.password() == null || request.password().isBlank()) {
//            return ResponseEntity.badRequest().body("Le mot de passe est obligatoire.");
//        }
//        try {
//            UtilisateurEntity user = userService.findByEmail(request.email());
//
//            if (passwordEncoder.matches(request.password(), user.getPassword())) {
//                String accessToken = jwtService.generateToken(user);
//                String refreshToken = jwtService.generateRefreshToken(user);
//
//                // Stockage dans Redis
//                tokenStore.storeToken(user.getEmail(), accessToken, Duration.ofMillis(jwtService.getExpiration()));
//                tokenStore.storeRefreshToken(user.getEmail(), refreshToken, Duration.ofMillis(jwtService.getRefreshExpiration()));
//
//                // Définition des cookies
//                setCookie(response, "access_token", accessToken, jwtService.getExpiration() / 1000);
//                setCookie(response, "refresh_token", refreshToken, jwtService.getRefreshExpiration() / 1000);
//
//                // Gestion des rôles
//                String role = String.valueOf(user.getRole());
//                String redirectUrl = "/api/dashboard"; // Par défaut
//
//                if ("professeur".equals(role)) {
//                    if (user.getProfesseur() == null) {
//                        redirectUrl = "/api/completeProfile/professeur";
//                    } else if (user.getProfesseur().getNom() != null) {
//                        redirectUrl = "/api/professeurs/dashboard";
//                    }
//                } else if ("etudiant".equals(role)) {
//                    if (user.getEtudiant() == null) {
//                        redirectUrl = "/api/completeProfile/etudiant";
//                    } else if (user.getEtudiant().getNom() != null) {
//                        redirectUrl = "/api/etudiants/dashboard";
//                    }
//                }
//
//                // Réponse avec rôle, token et redirection
//                Map<String, Object> responseBody = new HashMap<>();
//                responseBody.put("accessToken", accessToken);
//                responseBody.put("refreshToken", refreshToken);
//                responseBody.put("role", role);
//                responseBody.put("redirectUrl", redirectUrl);
//
//                return ResponseEntity.ok(responseBody);
//            }
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects.");
//
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé.");
//        }
//    }

}
