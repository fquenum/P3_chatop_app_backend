package com.chatop.service;

import com.chatop.dto.MessageRequest;
import com.chatop.model.Message;
import com.chatop.model.User;
import com.chatop.repository.MessageRepository;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service gérant la logique métier des messages
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    /**
     * Envoie un nouveau message sur une location
     *
     * @param request les données du message (rental_id, user_id, message)
     * @return le message créé
     * @throws IllegalArgumentException si la location ou l'utilisateur n'existe pas
     */
    @Transactional
    public Message sendMessage(MessageRequest request,String userEmail) {

        // 1. Vérification que la location existe
        if (!rentalRepository.existsById(request.getRentalId())) {
            throw new IllegalArgumentException("La location avec l'ID " + request.getRentalId() + " n'existe pas");
        }

        // 2. Récupération de l'utilisateur depuis l'email (JWT)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // 3. Création du message
        Message message = new Message();
        message.setRentalId(request.getRentalId());
        message.setUserId(user.getId());  // L'userId vient du JWT (pas du client)
        message.setMessage(request.getMessage());

        // 4. Dates automatiques
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());

        // 5. Sauvegarde en base de données
        return messageRepository.save(message);
    }
}