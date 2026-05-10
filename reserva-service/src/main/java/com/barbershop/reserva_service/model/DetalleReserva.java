// Archivo: DetalleReserva.java
package com.barbershop.reserva_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    @JsonIgnore
    private Reserva reserva;
    @Column(name = "id_producto", nullable = false)
    private Long idProducto;
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;
}