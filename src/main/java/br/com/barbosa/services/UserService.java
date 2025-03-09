package br.com.barbosa.services;

import br.com.barbosa.dtos.UpdateUserRequestDTO;
import br.com.barbosa.dtos.UserResponseDTO;
import br.com.barbosa.entities.Role;
import br.com.barbosa.entities.User;
import br.com.barbosa.exceptions.EmailAlreadyExistsException;
import br.com.barbosa.exceptions.PasswordValidationException;
import br.com.barbosa.exceptions.ResourceNotFoundException;
import br.com.barbosa.repositories.RoleRepository;
import br.com.barbosa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }

    public UserResponseDTO findById(String id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."));
        return UserResponseDTO.fromEntity(user);
    }

    public User create(User user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("O e-mail informado já está cadastrado. Por favor, utilize outro e-mail.");
        }

        validatePasswordLength(user.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Função de Usuário não encontrada."));
        user.getRoles().add(userRole);

        return repository.save(user);
    }

    public User update(String id, UpdateUserRequestDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."));

        if (!user.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("O e-mail informado já está cadastrado. Por favor, utilize outro e-mail.");
        }

        validatePasswordLength(dto.password());

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        return repository.save(user);
    }

    private void validatePasswordLength(String password) {
        if (password.length() < 8 || password.length() > 50) {
            throw new PasswordValidationException("A senha deve ter entre 8 e 50 caracteres.");
        }
    }

    public void deleteById(String id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getRoleName()));

        if (isAdmin) {
            throw new RuntimeException("Não é possível excluir o usuário administrador.");
        }

        repository.delete(user);
    }
}
