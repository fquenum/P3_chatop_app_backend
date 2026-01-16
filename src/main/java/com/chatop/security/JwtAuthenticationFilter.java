package com.chatop.security;
import com.chatop.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filtre JWT qui intercepte TOUTES les requêtes HTTP
 *
 * Ce filtre s'exécute AVANT que la requête n'atteigne le contrôleur.
 * Il extrait le token JWT du header Authorization et authentifie l'utilisateur.
 *
 * Flow :
 * 1. Récupère le header "Authorization: Bearer <token>"
 * 2. Extrait le token
 * 3. Valide le token avec JwtUtil
 * 4. Charge l'utilisateur depuis la BDD
 * 5. Place l'authentification dans le SecurityContext
 * 6. La requête continue vers le contrôleur
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Récupération du header Authorization
        final String authHeader = request.getHeader("Authorization");

        String jwt = null;
        String userEmail = null;

        // 2. Vérification du format "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);  // Extraction du token (après "Bearer ")

            try {
                // 3. Extraction de l'email du token
                userEmail = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token invalide ou expiré
                logger.error("Erreur lors de l'extraction du token JWT", e);
            }
        }

        // 4. Si un email est extrait et qu'aucune authentification n'existe déjà
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Chargement des détails de l'utilisateur depuis la BDD
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // 6. Validation du token
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // 7. Création de l'objet d'authentification Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,  // Pas de credentials
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. Stockage de l'authentification dans le SecurityContext
                // Maintenant Spring Security sait que l'utilisateur est authentifié
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. Poursuite de la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    }