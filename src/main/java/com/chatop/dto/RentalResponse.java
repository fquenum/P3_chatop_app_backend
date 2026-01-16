package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour la réponse contenant les informations d'une location
 * Utilisé pour les réponses GET /api/rentals et GET /api/rentals/:id
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Informations d'une annonce de location")
public class RentalResponse {

    @Schema(description = "Identifiant unique de la location", example = "1")
    private Long id;

    @Schema(description = "Nom de l'annonce", example = "Appartement lumineux")
    private String name;

    @Schema(description = "Surface en m²", example = "65.5")
    private BigDecimal surface;

    @Schema(description = "Prix mensuel en euros", example = "850.00")
    private BigDecimal price;

    @Schema(description = "URL de l'image de la location", example = "http://localhost:3001/uploads/image123.jpg")
    private String picture;

    @Schema(description = "Description détaillée", example = "Bel appartement T3")
    private String description;

    @Schema(description = "ID du propriétaire", example = "1")
    @JsonProperty("owner_id")
    private Long ownerId;

    @Schema(description = "Date de création", example = "2024/12/09")
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    @Schema(description = "Date de mise à jour", example = "2024/12/09")
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updatedAt;
}