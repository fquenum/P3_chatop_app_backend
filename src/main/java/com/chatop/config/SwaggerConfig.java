package com.chatop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de Swagger/OpenAPI pour la documentation de l'API
 *
 * Swagger génère automatiquement une documentation interactive de ton API
 * Accessible à l'URL : http://localhost:3001/swagger-ui/index.html
 *
 *  * Cette configuration :
 *  * - Définit les informations générales de l'API
 *  * - Configure l'authentification JWT
 *  * - Permet de tester les routes directement depuis l'interface
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configuration principale de l'API OpenAPI
     *
     * @return Configuration OpenAPI avec les informations de l'API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Informations générales sur l'API
                .info(new Info()
                        .title("ChâTop API")
                        .version("1.0.0")
                        .description("""
                                API REST pour la plateforme de location immobilière ChâTop.""")
                        .contact(new Contact()
                                .name("fabrice")
                                .email("quenum94@gmail.com")
                                .url("https://github.com/fquenum/P3_chatop_app_backend.git"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))

                // Configuration de la sécurité JWT
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))

                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                createAPIKeyScheme()));
    }

    /**
     * Définit le schéma de sécurité JWT
     * Ajoute un champ "Authorization" dans l'interface Swagger
     * pour tester les routes protégées
     *
     * @return Configuration du schéma de sécurité JWT
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("""
                        Entrez le token JWT obtenu après login ou register.
                        
                        Format : Collez uniquement le token (sans 'Bearer')
                        Exemple : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                        
                        Le préfixe 'Bearer' sera ajouté automatiquement.
                        """);
    }
}