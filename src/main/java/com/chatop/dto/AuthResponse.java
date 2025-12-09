package com.chatop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse d'authentification
 * Contient le token JWT après inscription ou connexion
 */
@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
}

