package com.chatop.controller;

import com.chatop.dto.AuthResponse;
import com.chatop.dto.LoginRequest;
import com.chatop.dto.RegisterRequest;
import com.chatop.dto.UserResponse;
import com.chatop.service.AuthService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour l'authentification
 *
 * Routes disponibles :
 * - POST /api/auth/register : Inscription (publique)
 * - POST /api/auth/login : Connexion (publique)
 * - GET /api/auth/me : Informations utilisateur connecté (protégée)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'inscription, la connexion et la gestion du profil utilisateur")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/register")
    @Operation(
            summary = "Inscription d'un nouvel utilisateur",
            description = """
                    Crée un nouveau compte utilisateur avec un nom, email et mot de passe.
                    
                    **Processus :**
                    1. Validation des données (email valide, mot de passe 6+ caractères)
                    2. Vérification que l'email n'existe pas déjà
                    3. Hashage du mot de passe avec BCrypt
                    4. Sauvegarde en base de données
                    5. Génération d'un token JWT
                    
                    **Le token JWT retourné permet d'accéder aux routes protégées.**
                    
                    Durée de validité du token : 24 heures
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inscription réussie - Token JWT retourné",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzMzNzU4MjAwLCJleHAiOjE3MzM4NDQ2MDB9.Xyz..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Email déjà utilisé ou données manquantes/invalides",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Email déjà utilisé",
                                            value = "{}"
                                    ),
                                    @ExampleObject(
                                            name = "Validation échouée",
                                            value = """
                                                    {
                                                      "name": "Le nom est obligatoire",
                                                      "email": "L'email doit être valide"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données d'inscription",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "name": "John Doe",
                                              "email": "john@example.com",
                                              "password": "password123"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody RegisterRequest request
    ) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new HashMap<>());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<>());
        }
    }

    /**
     * POST /api/auth/login
     * Connexion d'un utilisateur existant
     */
    @PostMapping("/login")
    @Operation(
            summary = "Connexion d'un utilisateur existant",
            description = """
                    Authentifie un utilisateur avec son email et mot de passe.
                    
                    **Processus :**
                    1. Validation des données
                    2. Vérification de l'existence de l'email
                    3. Comparaison du mot de passe avec le hash BCrypt en BDD
                    4. Génération d'un token JWT si authentification réussie
                    
                    **Le token JWT retourné permet d'accéder aux routes protégées.**
                    
                    Durée de validité du token : 24 heures
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Connexion réussie - Token JWT retourné",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification échouée - Email inexistant ou mot de passe incorrect",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "message": "error"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Identifiants de connexion",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "email": "john@example.com",
                                              "password": "password123"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody LoginRequest request
    ) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * GET /api/auth/me
     * Récupère les informations de l'utilisateur connecté
     */
    @GetMapping("/me")
    @Operation(
            summary = "Récupération des informations de l'utilisateur connecté",
            description = """
                    Retourne les informations du profil de l'utilisateur authentifié.
                    
                    **⚠️ Route protégée** : Nécessite un token JWT valide dans le header Authorization.
                    
                    **Processus :**
                    1. Le JwtAuthenticationFilter extrait et valide le token
                    2. L'email est extrait du token
                    3. Les informations utilisateur sont chargées depuis la BDD
                    4. Les données sont retournées (SANS le mot de passe)
                    
                    **Comment utiliser :**
                    1. Obtenez un token via `/api/auth/register` ou `/api/auth/login`
                    2. Cliquez sur "Authorize" 🔓 en haut de cette page
                    3. Collez le token et cliquez "Authorize"
                    4. Testez cette route
                    """,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Informations utilisateur récupérées avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": 1,
                                              "name": "John Doe",
                                              "email": "john@example.com",
                                              "createdAt": "2024-12-09T14:30:00",
                                              "updatedAt": "2024-12-09T14:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non autorisé - Token manquant, invalide ou expiré",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2024-12-09T15:30:00.000+00:00",
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "path": "/api/auth/me"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit - Token invalide",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        UserResponse user = authService.getCurrentUser(email);
        return ResponseEntity.ok(user);
    }
}