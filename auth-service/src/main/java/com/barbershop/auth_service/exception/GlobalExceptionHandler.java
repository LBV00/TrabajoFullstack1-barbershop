package com.barbershop.auth_service.exception;

import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");

        log.warn("Validación fallida en {}: {}", request.getRequestURI(), mensaje);

        return buildResponse(HttpStatus.BAD_REQUEST, mensaje, request.getRequestURI());
    }

  
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleCredencialesInvalidas(
            RuntimeException ex,
            HttpServletRequest request) {

        String mensaje = ex.getMessage();
        log.warn("Error de autenticación en {}: {}", request.getRequestURI(), mensaje);

        // Si el mensaje indica credenciales o usuario inactivo → 401
        if (mensaje != null && (mensaje.contains("Credenciales") || mensaje.contains("inactivo"))) {
            return buildResponse(HttpStatus.UNAUTHORIZED, mensaje, request.getRequestURI());
        }

        // Cualquier otro RuntimeException → 400 Bad Request
        return buildResponse(HttpStatus.BAD_REQUEST, mensaje, request.getRequestURI());
    }

  
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(
            Exception ex,
            HttpServletRequest request) {

        log.error("Error interno en {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor: " + ex.getMessage(),
                request.getRequestURI());
    }



    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status, String message, String path) {

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();

        return new ResponseEntity<>(error, status);
    }
}
