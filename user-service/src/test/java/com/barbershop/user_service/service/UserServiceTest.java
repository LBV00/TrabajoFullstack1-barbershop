package com.barbershop.user_service.service;

import com.barbershop.user_service.model.User;
import com.barbershop.user_service.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void debeListarUsuarios() {

        User user1 = User.builder()
                .id(1L)
                .nombre("Martin")
                .build();

        User user2 = User.builder()
                .id(2L)
                .nombre("Juan")
                .build();

        when(userRepository.findAll())
                .thenReturn(Arrays.asList(user1, user2));

        List<User> resultado = userService.findAll();

        assertEquals(2, resultado.size());

        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void debeBuscarUsuarioPorId() {

        User user = User.builder()
                .id(1L)
                .nombre("Martin")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User resultado = userService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void debeGuardarUsuario() {

        User user = User.builder()
                .nombre("Martin")
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        User resultado = userService.save(user);

        assertNotNull(resultado);
        assertEquals("Martin", resultado.getNombre());
    }

    @Test
    void debeEliminarUsuario() {

        userService.delete(1L);

        verify(userRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void debeVerificarExistenciaUsuario() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        Boolean existe = userService.existsById(1L);

        assertTrue(existe);
    }
}