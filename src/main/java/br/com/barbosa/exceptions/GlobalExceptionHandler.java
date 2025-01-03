package br.com.barbosa.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Exceção personalizada para e-mails já existentes
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("error", "EmailAlreadyExists");
        response.put("status", "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Captura exceções de integridade do banco (como violação de unicidade)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "O e-mail informado já está cadastrado. Por favor, utilize outro e-mail.");
        response.put("error", "EmailAlreadyExists");
        response.put("status", "400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Exceção genérica
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.");
        response.put("error", "InternalServerError");
        response.put("status", "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
