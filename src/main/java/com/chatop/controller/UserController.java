package com.chatop.controller;

import com.chatop.dto.UserResponse;
import com.chatop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des utilisateurs
 *
 * Routes disponibles (protégées par JWT) :
 * - GET /api/user/:id : Récupérer les informations d'un utilisateur
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Consultation des profils utilisateurs")
public class UserController {

    private final UserService userService;

    /**
     * GET /api/user/:id
     * Récupérer les informations d'un utilisateur par son ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupération d'un utilisateur par son ID",
            description = """
                    Retourne les informations publiques d'un utilisateur (nom, email, dates).
                    
                    **Cas d'usage** :
                    - Afficher le profil d'un propriétaire d'annonce
                    - Voir les informations de contact d'un utilisateur
                    - Vérifier l'existence d'un utilisateur
                    
                    **⚠️ Route protégée** : Nécessite un token JWT valide.
                    
                    **Note** : Le mot de passe n'est JAMAIS retourné (sécurité).
                    """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": 1,
                                              "name": "Alice Dupont",
                                              "email": "alice@example.com",
                                              "created_at": "2024-12-09T14:30:00",
                                              "updated_at": "2024-12-09T14:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-12-09T15:30:00.000+00:00",
                                              "status": 404,
                                              "error": "Not Found",
                                              "path": "/api/user/999"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non autorisé - Token manquant ou invalide"
            )
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}