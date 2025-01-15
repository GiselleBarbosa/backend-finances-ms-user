package br.com.barbosa.controllers;

import br.com.barbosa.dtos.UpdateUserRequestDTO;
import br.com.barbosa.entities.User;
import br.com.barbosa.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody User user) {
        User createdUser = service.create(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuário criado com sucesso.");
        response.put("user", createdUser);

        return ResponseEntity
                .created(URI.create("/users/" + createdUser.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequestDTO userDTO) {
        User updatedUser = service.update(id, userDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuário atualizado com sucesso.");
        response.put("user", updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
