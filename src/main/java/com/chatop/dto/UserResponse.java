package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse contenant les informations utilisateur
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Informations de profil de l'utilisateur")
public class UserResponse {

    @Schema(
            description = "Identifiant unique de l'utilisateur",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nom complet de l'utilisateur",
            example = "John Doe"
    )
    private String name;

    @Schema(
            description = "Adresse email de l'utilisateur",
            example = "john@example.com"
    )
    private String email;

    @JsonProperty(value = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy/MM/dd")
    @Schema(description = "Date et heure de création du compte", example = "2024-12-09T14:30:00")
    private LocalDateTime createdAt;

    @JsonProperty(value = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy/MM/dd")
    @Schema(description = "Date et heure de dernière mise à jour du profil",example = "2024-12-09T14:30:00")
    private LocalDateTime updatedAt;
}