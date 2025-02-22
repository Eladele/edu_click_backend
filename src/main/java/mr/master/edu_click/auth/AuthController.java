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

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UtilisateurService userService;

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String refreshToken, HttpServletResponse response) {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid or expired refresh token"));
        }
        String username = jwtService.extractUsername(refreshToken);
        UtilisateurEntity user = userService.findByEmail(username);
        String newAccessToken = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("access_token", newAccessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            Map<String, String> tokens = userService.authenticateUser(request.email(), request.password());
            String accessToken = tokens.get("access_token");
            String refreshToken = tokens.get("refresh_token");

            userService.addCookie(response, "access_token", accessToken);
            userService.addCookie(response, "refresh_token", refreshToken);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody LoginRequest request) {
//        try {
//            userService.registerUser(request.email(), request.password(), "USER");
//            return ResponseEntity.ok("User registered successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body(e.getMessage());
//        }
//    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        try {
            userService.logout(response);
            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
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
