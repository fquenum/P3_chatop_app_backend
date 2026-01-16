package com.chatop.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(unique = true, nullable = false,length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =========================================================================
    // IMPLÉMENTATION DE UserDetails (requis par Spring Security)
    // =========================================================================

    /**
     * Retourne les rôles/permissions de l'utilisateur
     * Pour l'instant, on retourne une liste vide (pas de gestion de rôles)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Retourne le "username" utilisé pour l'authentification
     * Dans notre cas, c'est l'EMAIL qui sert d'identifiant
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Le compte n'est jamais expiré
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Le compte n'est jamais verrouillé
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Les credentials ne sont jamais expirées
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Le compte est toujours activé
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Getter pour password
     * Lombok génère déjà getPassword() via @Data,
     * mais UserDetails l'exige aussi, donc c'est compatible
     */
    @Override
    public String getPassword() {
        return password;
    }


}

