package com.barbershop.auth_service.service;

import com.barbershop.auth_service.dto.LoginRequest;
import com.barbershop.auth_service.dto.LoginResponse;
import com.barbershop.auth_service.model.UsuarioAuth;
import com.barbershop.auth_service.repository.UsuarioAuthRepository;
import com.barbershop.auth_service.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UsuarioAuthRepository repo;
    private final JwtUtil jwtUtil;
    private final HashService hashService;

    public AuthService(UsuarioAuthRepository repo, JwtUtil jwtUtil, HashService hashService) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
        this.hashService = hashService;
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsername());

        UsuarioAuth usuario = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login fallido: usuario '{}' no existe", request.getUsername());
                    return new RuntimeException("Credenciales inválidas");
                });

        if (!usuario.getActivo()) {
            log.warn("Login fallido: usuario '{}' está inactivo", request.getUsername());
            throw new RuntimeException("Usuario inactivo");
        }

        if (!hashService.sha1(request.getPassword()).equals(usuario.getPassword())) {
            log.warn("Login fallido: contraseña incorrecta para '{}'", request.getUsername());
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol());
        log.info("Login exitoso para usuario: {}", request.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .mensaje("Login exitoso")
                .build();
    }

    public boolean validarToken(String token) { return jwtUtil.validateToken(token); }
    public String getUsernameDesdeToken(String token) { return jwtUtil.getUsernameFromToken(token); }
}