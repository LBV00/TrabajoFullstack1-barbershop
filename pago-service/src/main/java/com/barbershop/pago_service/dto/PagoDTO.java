package com.barbershop.pago_service.dto;

import com.barbershop.pago_service.model.Pago;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos de un pago")
public class PagoDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario", example = "1")
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @Schema(description = "ID de la reserva", example = "1")
    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    @Schema(description = "Monto del pago", example = "25000")
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @Schema(description = "Método de pago", example = "Tarjeta")
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