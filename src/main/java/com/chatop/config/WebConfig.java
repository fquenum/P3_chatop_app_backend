package com.chatop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Configuration pour servir les fichiers statiques uploadés
 *
 * Permet d'accéder aux images via :
 * http://localhost:3001/uploads/nom-fichier.jpg
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convertir le chemin relatif en chemin absolu
        String absolutePath = "file:" + Paths.get(uploadPath).toAbsolutePath() + "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(absolutePath);
    }
}