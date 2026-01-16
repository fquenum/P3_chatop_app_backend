package com.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO pour la création et modification d'une location
 * Utilisé pour POST /api/rentals et PUT /api/rentals/:id
 */
@Data
@Schema(description = "Données pour créer ou modifier une annonce de location")
public class RentalRequest {

    @Schema(
            description = "Nom/titre de l'annonce",
            example = "Appartement lumineux centre ville",
            required = true
    )
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255)
    private String name;

    @Schema(
            description = "Surface en m²",
            example = "65.5",
            required = true
    )
    @NotNull(message = "La surface est obligatoire")
    private BigDecimal surface;

    @Schema(
            description = "Prix par mois en euros",
            example = "850.00",
            required = true
    )
    @NotNull(message = "Le prix est obligatoire")
    private BigDecimal price;

    @Schema(
            description = "Description détaillée de la location",
            example = "Bel appartement T3 avec balcon, proche transports",
            maxLength = 2000
    )
    @Size(max = 2000)
    private String description;
}