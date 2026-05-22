package com.barbershop.auth_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios_auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol;

    @Column(nullable = false)
    private Boolean activo;
}