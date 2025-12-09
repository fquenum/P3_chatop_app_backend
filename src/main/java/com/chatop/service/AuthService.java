package com.chatop.service;
import com.chatop.dto.RegisterRequest;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service gérant la logique métier de l'authentification
 *
 * VERSION SIMPLE : Sans JWT et sans hashage BCrypt
 * ⚠️ ATTENTION : Cette version n'est PAS sécurisée !
 * Le mot de passe est stocké en CLAIR dans la base de données.
 *
 * On ajoutera la sécurité (BCrypt + JWT) dans une version ultérieure.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    /**
     * Inscription d'un nouvel utilisateur
     *
     * @param request les données d'inscription (name, email, password)
     * @return l'utilisateur créé
     * @throws IllegalArgumentException si l'email existe déjà
     */
    @Transactional
    public User register(RegisterRequest request) {

        // 1. Vérification que l'email n'existe pas déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        // 2. Création de l'utilisateur
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // ⚠️ VERSION SIMPLE : Mot de passe en CLAIR (NON sécurisé)
        // Dans la version avec sécurité, on utilisera :
        // user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassword(request.getPassword());

        // 3. Dates automatiques
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 4. Sauvegarde en base de données
        User savedUser = userRepository.save(user);

        // 5. Retour de l'utilisateur créé
        return savedUser;
    }
}