package com.chatop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité TEMPORAIRE
 * ⚠️ Toutes les routes sont accessibles sans authentification
 * À remplacer par une vraie configuration JWT plus tard
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive CSRF pour les tests
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Autorise toutes les requêtes
                );

        return http.build();
    }
}