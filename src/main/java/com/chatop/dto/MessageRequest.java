package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO pour l'envoi d'un message
 * Utilisé pour POST /api/messages
 */
@Data
@Schema(description = "Données pour envoyer un message sur une location")
public class MessageRequest {

    @Schema(
            description = "ID de la location concernée",
            example = "1",
            required = true
    )
    @NotNull(message = "L'ID de la location est obligatoire")
    @JsonProperty("rental_id")
    private Long rentalId;

    @Schema(
            description = "Contenu du message",
            example = "Bonjour, je suis intéressé par cette location. Est-elle toujours disponible ?",
            required = true,
            maxLength = 2000
    )
    @NotBlank(message = "Le message est obligatoire")
    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères")
    private String message;
}