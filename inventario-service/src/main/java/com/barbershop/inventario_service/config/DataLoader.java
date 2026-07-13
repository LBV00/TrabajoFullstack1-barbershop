package com.barbershop.inventario_service.config;

import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final InventarioRepository inventarioRepository;

    public DataLoader(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public void run(String... args) {

        if (inventarioRepository.count() >= 8) {
            log.info("DataLoader: inventario ya tiene datos completos, omitiendo carga.");
            return;
        }

        log.info("DataLoader: cargando registros adicionales de inventario...");

        inventarioRepository.save(
                Inventario.builder()
                        .productoId(6L)
                        .stock(18)
                        .ubicacion("Sucursal Providencia")
                        .build()
        );

        inventarioRepository.save(
                Inventario.builder()
                        .productoId(7L)
                        .stock(30)
                        .ubicacion("Sucursal Las Condes")
                        .build()
        );

        inventarioRepository.save(
                Inventario.builder()
                        .productoId(8L)
                        .stock(12)
                        .ubicacion("Bodega Central")
                        .build()
        );

        log.info("DataLoader: 3 registros adicionales de inventario cargados exitosamente.");
    }
}
