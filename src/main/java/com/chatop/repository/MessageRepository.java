package com.chatop.repository;

import com.chatop.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Message
 * JpaRepository fournit automatiquement les méthodes CRUD
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Trouve tous les messages d'une location
     * Méthode dérivée automatiquement par Spring Data JPA
     *
     * @param rentalId l'ID de la location
     * @return liste des messages de la location
     */
    List<Message> findByRentalId(Long rentalId);

    /**
     * Trouve tous les messages envoyés par un utilisateur
     *
     * @param userId l'ID de l'utilisateur
     * @return liste des messages de l'utilisateur
     */
    List<Message> findByUserId(Long userId);
}