package com.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour la réponse de la liste des locations
 * Format : { "rentals": [...] }
 * Utilisé pour GET /api/rentals
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Liste des annonces de location")
public class RentalsListResponse {

    @Schema(description = "Liste des locations")
    private List<RentalResponse> rentals;
}