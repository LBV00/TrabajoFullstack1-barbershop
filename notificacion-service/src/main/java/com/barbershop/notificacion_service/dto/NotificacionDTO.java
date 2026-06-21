package com.barbershop.notificacion_service.dto;

import com.barbershop.notificacion_service.model.Notificacion;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {

    private Long id;

    private String destinatario;

    private String mensaje;

    private String estado;

    public static NotificacionDTO fromModel(Notificacion n){
        return new NotificacionDTO(
                n.getId(),
                n.getDestinatario(),
                n.getMensaje(),
                n.getEstado()
        );
    }

    public Notificacion toModel(){
        return new Notificacion(
                id,
                destinatario,
                mensaje,
                estado
        );
    }
}