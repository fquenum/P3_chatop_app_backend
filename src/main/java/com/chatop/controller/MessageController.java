package com.chatop.controller;

import com.chatop.dto.MessageRequest;
import com.chatop.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des messages
 *
 * Routes disponibles (protégées par JWT) :
 * - POST /api/messages : Envoyer un message sur une location
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Gestion des messages entre utilisateurs et propriétaires")
public class MessageController {

    private final MessageService messageService;

    /**
     * POST /api/messages
     * Envoyer un message sur une location
     */
    @PostMapping
    @Operation(
            summary = "Envoi d'un message sur une location",
            description = """
                    Permet à un utilisateur d'envoyer un message concernant une location.
                    
                    **Cas d'usage** :
                    - Demande d'informations sur une location
                    - Prise de contact avec le propriétaire
                    - Questions sur la disponibilité
                    
                    **⚠️ Route protégée** : Nécessite un token JWT valide.
                    
                    **Note** : Le message est enregistré en base de données mais aucune notification
                    n'est envoyée dans cette version (à implémenter : emails, notifications push, etc.).
                    """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Message envoyé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "message": "Message send with success"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Données manquantes, rental_id ou user_id inexistant",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "error": "La location avec l'ID 999 n'existe pas"
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
    public ResponseEntity<?> sendMessage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du message à envoyer",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MessageRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "rental_id": 1,
                                              "user_id": 2,
                                              "message": "Bonjour, je suis intéressé par cette location. Est-elle toujours disponible ?"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody MessageRequest request,Authentication authentication //récupération du user depuis le JWT
    ) {
        try {
            // Extraction de l'email depuis le token JWT
            String userEmail = authentication.getName();

            // Envoi du message avec l'email authentifié
            messageService.sendMessage(request,userEmail);

            // Réponse au format Mockoon
            Map<String, String> response = new HashMap<>();
            response.put("message", "Message send with success");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Rental ou User inexistant
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            // Autre erreur
            return ResponseEntity.badRequest().body(new HashMap<>());
        }
    }
}