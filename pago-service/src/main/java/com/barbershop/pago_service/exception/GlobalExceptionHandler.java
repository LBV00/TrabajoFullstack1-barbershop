package com.barbershop.pago_service.exception;

import java.time.OffsetDateTime;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        log.warn("Bad request en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce("", (a, b) -> a.isEmpty() ? b : a + " | " + b);
        log.warn("Validación fallida en {}: {}", request.getRequestURI(), msg);
        return buildResponse(HttpStatus.BAD_REQUEST, msg, request.getRequestURI());
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleWebClientResponse(WebClientResponseException ex, HttpServletRequest request) {
        log.error("Error respuesta WebClient en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_GATEWAY,
                "Error de comunicación con servicio externo: " + ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleWebClientRequest(WebClientRequestException ex, HttpServletRequest request) {
        log.error("Error conexión WebClient en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_GATEWAY,
                "No se pudo conectar con el servicio externo: " + ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor: " + ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, String path) {
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