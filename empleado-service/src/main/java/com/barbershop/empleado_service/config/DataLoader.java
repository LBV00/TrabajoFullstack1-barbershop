package com.barbershop.empleado_service.config;

import com.barbershop.empleado_service.model.Empleado;
import com.barbershop.empleado_service.repository.EmpleadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final EmpleadoRepository empleadoRepository;

    public DataLoader(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public void run(String... args) {

        if (empleadoRepository.count() >= 7) {
            log.info("DataLoader: empleados ya tienen datos completos, omitiendo carga.");
            return;
        }

        log.info("DataLoader: cargando empleados adicionales...");

        empleadoRepository.save(
                Empleado.builder()
                        .nombre("Felipe Castillo")
                        .especialidad("Barbero Estilista")
                        .telefono("956789012")
                        .disponible(true)
                        .build()
        );

        empleadoRepository.save(
                Empleado.builder()
                        .nombre("Rodrigo Fuentes")
                        .especialidad("Especialista en Coloración")
                        .telefono("967890123")
                        .disponible(false)
                        .build()
        );

        log.info("DataLoader: 2 empleados adicionales cargados exitosamente.");
    }
}
