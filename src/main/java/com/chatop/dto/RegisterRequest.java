package com.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO pour la requête d'inscription
 */
@Data
@Schema(description = "Données requises pour l'inscription d'un nouvel utilisateur")
public class RegisterRequest {

    @Schema(
            description = "Nom complet de l'utilisateur",
            example = "John Doe",
            required = true,
            maxLength = 255
    )
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
    private String name;

    @Schema(
            description = "Adresse email de l'utilisateur (doit être unique)",
            example = "john@example.com",
            required = true,
            maxLength = 255
    )
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
    private String email;

    @Schema(
            description = "Mot de passe de l'utilisateur (sera hashé avec BCrypt)",
            example = "password123",
            required = true,
            minLength = 6
    )
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
}