package com.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse d'authentification
 */
@Data
@AllArgsConstructor
@Schema(description = "Réponse d'authentification contenant le token JWT")
public class AuthResponse {

    @Schema(
            description = "Token JWT pour l'authentification. Utilisez ce token dans le header Authorization: Bearer <token>",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzMzNzU4MjAwLCJleHAiOjE3MzM4NDQ2MDB9.Xyz...",
            required = true
    )
    private String token;
}