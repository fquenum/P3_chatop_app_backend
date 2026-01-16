package com.chatop.service;

import com.chatop.dto.AuthResponse;
import com.chatop.dto.UserResponse;
import com.chatop.dto.LoginRequest;
import com.chatop.dto.RegisterRequest;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import com.chatop.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service gérant la logique métier de l'authentification
 *
 * VERSION SÉCURISÉE avec :
 * - BCrypt pour hasher les mots de passe
 * - JWT pour générer des tokens
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // BCrypt
    private final JwtUtil jwtUtil;                  // Génération JWT
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    /**
     * Inscription d'un nouvel utilisateur
     *
     * @param request les données d'inscription (name, email, password)
     * @return AuthResponse contenant le token JWT
     * @throws IllegalArgumentException si l'email existe déjà
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // 1. Vérification que l'email n'existe pas déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        // 2. Création de l'utilisateur avec le mot de passe HASHÉ
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // SÉCURISÉ : Hashage du mot de passe avec BCrypt
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Dates automatiques
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 4. Sauvegarde en base de données
        userRepository.save(user);

        // 5. Génération du token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        // 6. Retour du token
        return new AuthResponse(token);
    }

    /**
     * Connexion d'un utilisateur existant
     *
     * Cette méthode :
     * 1. Vérifie que l'email existe
     * 2. Vérifie que le mot de passe correspond (BCrypt compare automatiquement)
     * 3. Génère un token JWT si OK
     *
     * @param request les identifiants (email, password)
     * @return AuthResponse contenant le token JWT
     * @throws BadCredentialsException si les identifiants sont incorrects
     */
    public AuthResponse login(LoginRequest request) {
        try {
            // 1. Authentification avec Spring Security
            // AuthenticationManager va :
            //    - Charger l'utilisateur avec CustomUserDetailsService
            //    - Comparer le mot de passe avec BCrypt
            //    - Lever une exception si les identifiants sont incorrects
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 2. Si l'authentification réussit, génération du token JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            return new AuthResponse(token);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }
    }
    /**
     * Récupère les informations de l'utilisateur connecté
     *
     * L'email de l'utilisateur est extrait du token JWT par le JwtAuthenticationFilter
     * et placé dans le SecurityContext de Spring Security.
     *
     * @param email l'email de l'utilisateur (extrait du token JWT)
     * @return UserResponse contenant les informations de l'utilisateur
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    public UserResponse getCurrentUser(String email) {
        // 1. Recherche de l'utilisateur par email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // 2. Création du DTO avec les informations (SANS le mot de passe)
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}