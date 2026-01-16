package com.chatop.service;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service personnalisé pour charger les utilisateurs depuis la BDD
 * Implémente UserDetailsService de Spring Security
 *
 * Spring Security utilise ce service pour :
 * - Authentifier un utilisateur lors du login
 * - Valider un token JWT (vérifier que l'email existe)
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son email
     *
     * @param email l'email de l'utilisateur (appelé "username" par Spring Security)
     * @return UserDetails contenant les informations de l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Recherche l'utilisateur dans la BDD
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        // Création d'un UserDetails de Spring Security
        // On utilise l'email comme username
        // ArrayList vide car on ne gère pas les rôles dans ce projet
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()  // Pas de rôles (authorities)
        );
    }
}