package com.empresa.soporte_tecnico.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🟨 Error 400 - Datos inválidos en el request (validaciones fallidas)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("estado", HttpStatus.BAD_REQUEST.value());
        body.put("mensaje", "La solicitud contiene datos inválidos. Por favor, verifica los campos enviados.");
        body.put("ruta", request.getRequestURI());
        body.put("fecha", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 🟦 Error 404 - Recurso no encontrado
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        HttpStatus status = (HttpStatus) ex.getStatusCode();

        if (status == HttpStatus.NOT_FOUND) {
            body.put("estado", 404);
            body.put("mensaje", ex.getReason() != null
                    ? ex.getReason()
                    : "El recurso solicitado no fue encontrado en el sistema.");
        } else {
            body.put("estado", status.value());
            body.put("mensaje", ex.getReason() != null
                    ? ex.getReason()
                    : "Ocurrió un error con la solicitud.");
        }

        body.put("ruta", request.getRequestURI());
        body.put("fecha", LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }

    // 🟥 Error 500 - Error interno del servidor o excepciones inesperadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("estado", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("mensaje", "Ocurrió un error interno en el servidor. Porfavor no colocar ID porque se genera automaticamente, Inténtalo nuevamente.");
        body.put("ruta", request.getRequestURI());
        body.put("fecha", LocalDateTime.now());

        // Opcional: imprime en consola para debugging
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
