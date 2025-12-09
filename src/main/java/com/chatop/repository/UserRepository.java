package com.chatop.repository;
import com.chatop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité User
 * JpaRepository fournit automatiquement les méthodes CRUD
 *
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
    /**
     * Recherche un utilisateur par son email
     * Spring Data JPA génère automatiquement la requête SQL
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un email existe déjà
     * Utile pour valider l'unicité lors de l'inscription
     */
    boolean existsByEmail(String email);
}