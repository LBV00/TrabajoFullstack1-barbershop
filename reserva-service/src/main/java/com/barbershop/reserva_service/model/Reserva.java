// Archivo: Reserva.java
package com.barbershop.reserva_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;
    @Column(nullable = false)
    private Double total;
    @Column(nullable =false)
    private String estado;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleReserva> detalles;
}