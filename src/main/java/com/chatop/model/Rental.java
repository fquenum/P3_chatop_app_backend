package com.chatop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité Rental représentant une annonce de location
 * Liée à la table RENTALS en base de données
 */
@Entity
@Table(name = "RENTALS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @NotNull(message = "La surface est obligatoire")
    @Column(nullable = false)
    private BigDecimal surface;

    @NotNull(message = "Le prix est obligatoire")
    @Column(nullable = false)
    private BigDecimal price;

    @Size(max = 255)
    @Column(nullable = true)
    private String picture;

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

    @NotNull(message = "Le propriétaire est obligatoire")
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}