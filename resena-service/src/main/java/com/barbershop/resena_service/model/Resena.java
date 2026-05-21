package com.barbershop.resena_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(nullable = false)
    private Integer calificacion;

    @Column(nullable = false, length = 255)
    private String comentario;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}