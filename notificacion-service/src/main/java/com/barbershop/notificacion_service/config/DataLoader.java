package com.barbershop.notificacion_service.config;

import com.barbershop.notificacion_service.model.Notificacion;
import com.barbershop.notificacion_service.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final NotificacionRepository notificacionRepository;

    public DataLoader(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public void run(String... args) {

        if (notificacionRepository.count() >= 8) {
            log.info("DataLoader: notificaciones ya tienen datos completos, omitiendo carga.");
            return;
        }

        log.info("DataLoader: cargando notificaciones adicionales...");

        notificacionRepository.save(
                Notificacion.builder()
                        .destinatario("cliente5@gmail.com")
                        .mensaje("Su corte de cabello fue calificado con 5 estrellas")
                        .estado("ENVIADA")
                        .build()
        );

        notificacionRepository.save(
                Notificacion.builder()
                        .destinatario("cliente6@gmail.com")
                        .mensaje("Tiene una cita programada para mañana a las 10:00")
                        .estado("PENDIENTE")
                        .build()
        );

        notificacionRepository.save(
                Notificacion.builder()
                        .destinatario("cliente7@gmail.com")
                        .mensaje("Su reserva número 12 ha sido confirmada exitosamente")
                        .estado("ENVIADA")
                        .build()
        );

        log.info("DataLoader: 3 notificaciones adicionales cargadas exitosamente.");
    }
}
