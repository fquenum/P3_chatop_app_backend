package com.chatop.service;

import com.chatop.dto.UserResponse;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service gérant la logique métier des utilisateurs
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Récupère un utilisateur par son ID
     *
     * @param id l'identifiant de l'utilisateur
     * @return UserResponse contenant les informations de l'utilisateur
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + id));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}