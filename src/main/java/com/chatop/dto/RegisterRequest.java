package com.chatop.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) pour la requête d'inscription
 *
 * Ce DTO permet de :
 * - Valider automatiquement les données d'entrée avec @Valid
 * - Séparer la couche API de la couche base de données
 *
 */

@Data
public class RegisterRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
    private String name;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
}