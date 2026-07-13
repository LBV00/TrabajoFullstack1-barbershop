package com.barbershop.auth_service.service;

import com.barbershop.auth_service.dto.LoginRequest;
import com.barbershop.auth_service.dto.LoginResponse;
import com.barbershop.auth_service.model.UsuarioAuth;
import com.barbershop.auth_service.repository.UsuarioAuthRepository;
import com.barbershop.auth_service.security.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias aisladas para AuthService.
 * No usa base de datos; el repositorio, JwtUtil y HashService se simulan con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioAuthRepository repo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HashService hashService;

    @InjectMocks
    private AuthService authService;

    // ── login exitoso ────────────────────────────────────────────────────────

    @Test
    void debeRetornarTokenEnLoginExitoso() {

        UsuarioAuth usuario = UsuarioAuth.builder()
                .id(1L)
                .username("admin")
                .password("da39a3ee5e6b4b0d3255bfef95601890afd80709") // sha1 de "1234"
                .rol("ADMIN")
                .activo(true)
                .build();

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("1234");

        when(repo.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(hashService.sha1("1234"))
                .thenReturn("da39a3ee5e6b4b0d3255bfef95601890afd80709");
        when(jwtUtil.generateToken("admin", "ADMIN")).thenReturn("jwt-token-simulado");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token-simulado", response.getToken());
        assertEquals("admin", response.getUsername());
        assertEquals("ADMIN", response.getRol());
        assertEquals("Login exitoso", response.getMensaje());

        verify(repo, times(1)).findByUsername("admin");
        verify(hashService, times(1)).sha1("1234");
        verify(jwtUtil, times(1)).generateToken("admin", "ADMIN");
    }

    // ── login: usuario no existe ─────────────────────────────────────────────

    @Test
    void debeLanzarExcepcionSiUsuarioNoExiste() {

        LoginRequest request = new LoginRequest();
        request.setUsername("noExiste");
        request.setPassword("1234");

        when(repo.findByUsername("noExiste")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(repo, times(1)).findByUsername("noExiste");
        verifyNoInteractions(jwtUtil);
    }

    // ── login: usuario inactivo ──────────────────────────────────────────────

    @Test
    void debeLanzarExcepcionSiUsuarioEstaInactivo() {

        UsuarioAuth usuarioInactivo = UsuarioAuth.builder()
                .id(2L)
                .username("inactivo")
                .password("hash-cualquiera")
                .rol("USER")
                .activo(false)
                .build();

        LoginRequest request = new LoginRequest();
        request.setUsername("inactivo");
        request.setPassword("1234");

        when(repo.findByUsername("inactivo")).thenReturn(Optional.of(usuarioInactivo));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Usuario inactivo", ex.getMessage());
        verifyNoInteractions(jwtUtil);
    }

    // ── login: contraseña incorrecta ─────────────────────────────────────────

    @Test
    void debeLanzarExcepcionSiContrasenaEsIncorrecta() {

        UsuarioAuth usuario = UsuarioAuth.builder()
                .id(1L)
                .username("admin")
                .password("hash-correcto")
                .rol("ADMIN")
                .activo(true)
                .build();

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("contrasenaWrong");

        when(repo.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(hashService.sha1("contrasenaWrong")).thenReturn("hash-incorrecto");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Credenciales inválidas", ex.getMessage());
        verifyNoInteractions(jwtUtil);
    }

    // ── validarToken ─────────────────────────────────────────────────────────

    @Test
    void debeValidarTokenValido() {

        when(jwtUtil.validateToken("token-valido")).thenReturn(true);

        boolean resultado = authService.validarToken("token-valido");

        assertTrue(resultado);
        verify(jwtUtil, times(1)).validateToken("token-valido");
    }

    @Test
    void debeRetornarFalsoParaTokenInvalido() {

        when(jwtUtil.validateToken("token-expirado")).thenReturn(false);

        boolean resultado = authService.validarToken("token-expirado");

        assertFalse(resultado);
    }

    // ── getUsernameDesdeToken ─────────────────────────────────────────────────

    @Test
    void debeExtraerUsernameDesdeToken() {

        when(jwtUtil.getUsernameFromToken("mi-token")).thenReturn("admin");

        String username = authService.getUsernameDesdeToken("mi-token");

        assertEquals("admin", username);
        verify(jwtUtil, times(1)).getUsernameFromToken("mi-token");
    }
}
