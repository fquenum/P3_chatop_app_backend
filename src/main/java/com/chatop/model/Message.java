package com.chatop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité Message représentant un message envoyé sur une location
 * Liée à la table MESSAGES en base de données
 */
@Entity
@Table(name = "MESSAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID de la location est obligatoire")
    @Column(name = "rental_id", nullable = false)
    private Long rentalId;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "Le message est obligatoire")
    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères")
    @Column(length = 2000, nullable = false)
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}