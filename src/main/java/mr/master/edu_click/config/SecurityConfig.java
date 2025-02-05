package mr.master.edu_click.config;
import lombok.RequiredArgsConstructor;
import mr.master.edu_click.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour les API stateless
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activation CORS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Désactiver les sessions
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Autoriser l'accès à l'authentification
//                        .requestMatchers("/api/utilisateurs/**").authenticated() // Protéger les endpoints des utilisateurs
                        .anyRequest().permitAll() // Autoriser tous les autres endpoints sans authentification
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Ajouter le filtre JWT
                .build();
    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/utilisateurs/**").authenticated()  // Authentifie les accès aux utilisateurs
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }




//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    return http
//            .csrf(csrf -> csrf.disable())
//            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activation CORS
//            .sessionManagement(session -> session
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/api/auth/**").permitAll()
//                    .requestMatchers("/api/utilisateurs/**").authenticated()
//                    .anyRequest().authenticated())
//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//            .build();
//}

    // Configuration CORS
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8099")); // Autoriser Postman
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true); // Autoriser les cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}