package br.com.barbosa.dtos;

import br.com.barbosa.entities.User;

import java.time.LocalDateTime;

public record UserResponseDTO(String id, String name, String email, LocalDateTime criadoEm) {

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
