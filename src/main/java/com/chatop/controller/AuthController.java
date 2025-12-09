package com.chatop.controller;
import com.chatop.dto.RegisterRequest;
import com.chatop.model.User;
import com.chatop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour l'authentification
 *
 * VERSION SIMPLE : Sans JWT, juste pour tester l'inscription
 * Route disponible : POST /api/auth/register
 *
 * ⚠️ Cette version retourne l'utilisateur complet (avec mot de passe)
 * Dans la version sécurisée, on retournera un token JWT
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Inscription d'un nouvel utilisateur
     *
     * VERSION SIMPLE : Retourne l'utilisateur créé (avec ID et dates)
     *
     * @param request les données d'inscription
     * @return 200 OK avec l'utilisateur créé, ou 400 Bad Request si erreur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Appel au service pour créer l'utilisateur
            User user = authService.register(request);

            // Création de la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("created_at", user.getCreatedAt());
            response.put("updated_at", user.getUpdatedAt());
            response.put("message", "Utilisateur créé avec succès");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Email déjà existant
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            // Autre erreur
            return ResponseEntity.badRequest().body(new HashMap<>());
        }
    }

    /**
     * GET /api/test
     * Route de test simple pour vérifier que le serveur fonctionne
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Le serveur ChâTop fonctionne ! 🚀");
    }
}