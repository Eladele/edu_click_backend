package mr.master.edu_click.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisTokenStore tokenStore;

    public JwtAuthFilter(JwtService jwtService, RedisTokenStore tokenStore) {
        this.jwtService = jwtService;
        this.tokenStore = tokenStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        // 1. Vérifier dans l'Authorization Header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Token trouvé dans l'Authorization Header: " + token);
        }

        // 2. Vérifier dans les cookies si pas de token dans l'Authorization header
        if (token == null && request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(c -> "access_token".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (token != null) {
                System.out.println("Token trouvé dans le cookie: " + token);
            }
        }

        // 3. Valider le token
        if (token != null && jwtService.validateToken(token)) {
            String username = jwtService.extractUsername(token);
            List<String> roles = jwtService.extractRoles(token); // Méthode à ajouter dans JwtService

            if (tokenStore.validateToken(username, token)) {
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Utilisateur authentifié: " + username + " avec rôles: " + roles);
            } else {
                System.out.println("Token invalide ou expiré pour l'utilisateur: " + username);
            }
        } else {
            System.out.println("Aucun token valide trouvé.");
        }

        filterChain.doFilter(request, response);
    }
}
