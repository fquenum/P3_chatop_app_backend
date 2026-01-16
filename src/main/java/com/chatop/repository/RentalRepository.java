package com.chatop.repository;

import com.chatop.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Rental
 * JpaRepository fournit automatiquement les méthodes CRUD
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    /**
     * Trouve toutes les locations d'un propriétaire
     * Méthode dérivée automatiquement par Spring Data JPA
     *
     * @param ownerId l'ID du propriétaire
     * @return liste des locations du propriétaire
     */
    List<Rental> findByOwnerId(Long ownerId);
}