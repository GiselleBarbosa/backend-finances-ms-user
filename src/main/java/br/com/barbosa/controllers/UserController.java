package br.com.barbosa.controllers;

import br.com.barbosa.dtos.UpdateUserRequestDTO;
import br.com.barbosa.dtos.UserResponseDTO;
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
import java.lang.String;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<UserResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> findByIdAdmin(@PathVariable String id) {
        return ResponseEntity.ok(service.findByIdAdmin(id));
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
    public ResponseEntity<Map<String, Object>> update(@PathVariable String id, @Valid @RequestBody UpdateUserRequestDTO userDTO) {
        User updatedUser = service.update(id, userDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuário atualizado com sucesso.");
        response.put("user", updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
