package com.chatop.controller;

import com.chatop.dto.RentalRequest;
import com.chatop.dto.RentalResponse;
import com.chatop.dto.RentalsListResponse;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import com.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des locations
 * VERSION AVEC UPLOAD D'IMAGE
 */
@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals", description = "Gestion des annonces de location immobilière")
public class RentalController {

    private final RentalService rentalService;

    /**
     * GET /api/rentals
     * Liste de toutes les locations
     */
    @GetMapping
    @Operation(
            summary = "Récupération de toutes les locations",
            description = """
                    Retourne la liste complète de toutes les annonces de location disponibles.
                    
                    ** Route protégée** : Nécessite un token JWT valide.
                    """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des locations récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalsListResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non autorisé - Token manquant ou invalide"
            )
    })
    public ResponseEntity<RentalsListResponse> getAllRentals() {
        RentalsListResponse response = rentalService.getAllRentals();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/rentals/:id
     * Détails d'une location
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupération d'une location par son ID",
            description = """
                    Retourne les détails complets d'une annonce de location spécifique.
                    
                    ** Route protégée** : Nécessite un token JWT valide.
                    """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Location trouvée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Location non trouvée"
            )
    })
    public ResponseEntity<?> getRentalById(@PathVariable Long id) {
        try {
            RentalResponse response = rentalService.getRentalById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/rentals
     * Créer une nouvelle location AVEC upload d'image
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Création d'une nouvelle annonce de location avec image",
            description = """
                    Crée une nouvelle annonce de location avec upload d'image.
                    
                    **Format** : multipart/form-data
                    
                    **Champs** :
                    - name (string, requis) : Nom de l'annonce
                    - surface (number, requis) : Surface en m²
                    - price (number, requis) : Prix mensuel en euros
                    - description (string, optionnel) : Description détaillée
                    - picture (file, optionnel) : Image de la location (jpg, png, etc.)
                    
                    Le propriétaire (owner_id) est automatiquement défini comme l'utilisateur connecté.
                    
                    ** Route protégée** : Nécessite un token JWT valide.
                    """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Location créée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "message": "Rental created !"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Données manquantes ou image invalide"
            )
    })
    public ResponseEntity<?> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            Authentication authentication
    ) {
        try {
            // extraction de l'email depuis le JWT
            String userEmail = authentication.getName();

            // Création du DTO
            RentalRequest request = new RentalRequest();
            request.setName(name);
            request.setSurface(surface);
            request.setPrice(price);
            request.setDescription(description);

            // Création de la location avec l'email (le service récupère l'ownerId)
            rentalService.createRental(request, picture, userEmail);

            // Réponse au format Mockoon
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rental created !");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * PUT /api/rentals/:id
     * Modifier une location existante (SANS modification de l'image)
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Modification d'une annonce de location",
            description = """
                    Met à jour les informations d'une annonce de location existante.
                    
                    **Note** : L'image ne peut pas être modifiée (on garde l'ancienne).
                    
                    **Format** : multipart/form-data
                    
                    **Restriction** : Seul le propriétaire de l'annonce peut la modifier.
                    
                    **⚠️ Route protégée** : Nécessite un token JWT valide.
                    """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Location mise à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "message": "Rental updated !"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide ou utilisateur non autorisé"
            )
    })
    public ResponseEntity<?> updateRental(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication
    ) {
        try {
            //Extraction de l'email depuis JWT
            String userEmail = authentication.getName();

            // Création du DTO
            RentalRequest request = new RentalRequest();
            request.setName(name);
            request.setSurface(surface);
            request.setPrice(price);
            request.setDescription(description);

            // Mise à jour avec l'email (le service vérifie la propriété)
            rentalService.updateRental(id, request, userEmail);

            // Réponse au format Mockoon
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rental updated !");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}