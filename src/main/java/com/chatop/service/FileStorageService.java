package com.chatop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service pour gérer l'upload de fichiers (images)
 *
 * Fonctionnalités :
 * - Validation du type de fichier (images uniquement)
 * - Génération d'un nom unique pour éviter les conflits
 * - Stockage dans un dossier local
 * - Retour de l'URL publique du fichier
 */
@Service
@Slf4j
public class FileStorageService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url}")
    private String uploadUrl;

    /**
     * Sauvegarde un fichier uploadé
     *
     * @param file le fichier à sauvegarder
     * @return l'URL publique du fichier sauvegardé
     * @throws IOException si erreur lors de la sauvegarde
     * @throws IllegalArgumentException si le fichier n'est pas une image
     */
    public String saveFile(MultipartFile file) throws IOException {

        // 1. Validation : le fichier ne doit pas être vide
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide");
        }

        // 2. Validation : vérifier que c'est bien une image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Le fichier doit être une image (jpg, png, etc.)");
        }

        // 3. Créer le dossier uploads s'il n'existe pas
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("Dossier uploads créé : {}", uploadDir.toAbsolutePath());
        }

        // 4. Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // 5. Chemin complet du fichier
        Path filePath = uploadDir.resolve(uniqueFilename);

        // 6. Sauvegarder le fichier
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Fichier sauvegardé : {}", filePath.toAbsolutePath());

        // 7. Retourner l'URL publique
        return uploadUrl + uniqueFilename;
    }

    /**
     * Supprime un fichier par son URL
     * Utile si on veut implémenter la suppression de locations
     *
     * @param fileUrl l'URL du fichier à supprimer
     */
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith(uploadUrl)) {
                String filename = fileUrl.substring(uploadUrl.length());
                Path filePath = Paths.get(uploadPath).resolve(filename);
                Files.deleteIfExists(filePath);
                log.info("Fichier supprimé : {}", filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Erreur lors de la suppression du fichier : {}", fileUrl, e);
        }
    }
}