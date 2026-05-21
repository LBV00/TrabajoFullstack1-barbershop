package com.barbershop.pago_service.dto;

import com.barbershop.pago_service.model.Pago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDTO {
    
    private Long id;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    public static PagoDTO fromModel(Pago p) {
        return PagoDTO.builder()
                .id(p.getId())
                .idUsuario(p.getIdUsuario())
                .idReserva(p.getIdReserva())
                .monto(p.getMonto())
                .metodoPago(p.getMetodoPago())
                .build();
    }

    public Pago toModel() {
        return Pago.builder()
                .id(this.id)
                .idUsuario(this.idUsuario)
                .idReserva(this.idReserva)
                .monto(this.monto)
                .metodoPago(this.metodoPago)
                .build();
    }
}