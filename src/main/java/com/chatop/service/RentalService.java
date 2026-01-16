package com.chatop.service;

import com.chatop.dto.RentalRequest;
import com.chatop.dto.RentalResponse;
import com.chatop.dto.RentalsListResponse;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant la logique métier des locations
 * VERSION AVEC UPLOAD D'IMAGE
 */
@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    /**
     * Récupère toutes les locations
     */
    public RentalsListResponse getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();

        List<RentalResponse> rentalResponses = rentals.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new RentalsListResponse(rentalResponses);
    }

    /**
     * Récupère une location par son ID
     */
    public RentalResponse getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location non trouvée avec l'ID : " + id));

        return convertToResponse(rental);
    }

    /**
     * Crée une nouvelle location AVEC upload d'image
     *
     * @param request les données de la location
     * @param picture le fichier image (optionnel)
     * @param userEmail l'email du propriétaire (extrait du JWT)
     * @return RentalResponse contenant les informations de la location créée
     */
    @Transactional
    public RentalResponse createRental(RentalRequest request, MultipartFile picture, String userEmail) {

        // Récupération de l'utilisateur depuis l'email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        Rental rental = new Rental();
        rental.setName(request.getName());
        rental.setSurface(request.getSurface());
        rental.setPrice(request.getPrice());
        rental.setDescription(request.getDescription());
        rental.setOwnerId(user.getId()); //L'ownerId vient du JWT

        // Upload de l'image si présente
        if (picture != null && !picture.isEmpty()) {
            try {
                String pictureUrl = fileStorageService.saveFile(picture);
                rental.setPicture(pictureUrl);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l'upload de l'image : " + e.getMessage());
            }
        }

        // Dates automatiques
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        Rental savedRental = rentalRepository.save(rental);

        return convertToResponse(savedRental);
    }

    /**
     * Met à jour une location existante
     * Note : L'image ne peut pas être modifiée dans cette version
     */
    @Transactional
    public RentalResponse updateRental(Long id, RentalRequest request, String userEmail) {

        // Récupération de l'utilisateur depuis l'email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location non trouvée avec l'ID : " + id));

        // Vérification que l'utilisateur est bien le propriétaire
        if (!rental.getOwnerId().equals(user.getId())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier cette location");
        }

        // Mise à jour des champs
        rental.setName(request.getName());
        rental.setSurface(request.getSurface());
        rental.setPrice(request.getPrice());
        rental.setDescription(request.getDescription());
        rental.setUpdatedAt(LocalDateTime.now());

        // L'image n'est pas modifiable (on garde l'ancienne)

        Rental updatedRental = rentalRepository.save(rental);

        return convertToResponse(updatedRental);
    }

    /**
     * Convertit une entité Rental en RentalResponse
     */
    private RentalResponse convertToResponse(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwnerId(),
                rental.getCreatedAt(),
                rental.getUpdatedAt()
        );
    }
}