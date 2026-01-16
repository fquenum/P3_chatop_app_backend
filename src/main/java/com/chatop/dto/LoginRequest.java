package com.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO pour la requête de connexion
 */
@Data
@Schema(description = "Identifiants requis pour la connexion")
public class LoginRequest {

    @Schema(
            description = "Adresse email de l'utilisateur",
            example = "john@example.com",
            required = true
    )
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @Schema(
            description = "Mot de passe de l'utilisateur",
            example = "password123",
            required = true
    )
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}