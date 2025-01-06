package br.com.barbosa.services;

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
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."));
    }

    public List<User> findAll() {
        return repository.findAll();
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

    private void validatePasswordLength(String password) {
        if (password.length() < 8 || password.length() > 50) {
            throw new PasswordValidationException("A senha deve ter entre 8 e 50 caracteres.");
        }
    }

    public void deleteById(UUID id) {
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
