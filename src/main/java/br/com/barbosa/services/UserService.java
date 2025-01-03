package br.com.barbosa.services;

import br.com.barbosa.entities.Role;
import br.com.barbosa.entities.User;
import br.com.barbosa.exceptions.EmailAlreadyExistsException;
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

    public User findById(Long id) {
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

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Função de Usuário não encontrada."));
        user.getRoles().add(userRole);

        return repository.save(user);
    }

}
