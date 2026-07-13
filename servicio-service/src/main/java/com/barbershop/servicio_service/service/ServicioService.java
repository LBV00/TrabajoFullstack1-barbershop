package com.barbershop.servicio_service.service;

import com.barbershop.servicio_service.exception.BadRequestException;
import com.barbershop.servicio_service.exception.ResourceNotFoundException;
import com.barbershop.servicio_service.model.Servicio;
import com.barbershop.servicio_service.repository.ServicioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    private static final Logger logger =
            LoggerFactory.getLogger(ServicioService.class);

    @Autowired
    private ServicioRepository repository;

    public List<Servicio> findAll() {

        logger.info("Listando todos los servicios");

        return repository.findAll();
    }

    public Servicio findById(Long id) {

        logger.info("Buscando servicio con ID {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Servicio con ID {} no encontrado", id);

                    return new ResourceNotFoundException(
                            "Servicio con ID " + id + " no encontrado");
                });
    }

    /**
     * Regla de negocio 1: El precio debe ser mayor a cero.
     *   Un servicio con precio 0 o negativo no tiene sentido de negocio.
     * Regla de negocio 2: La duracion (en minutos) debe ser mayor a cero.
     *   Un servicio sin duracion minima no puede ser agendado.
     * Regla de negocio 3: El nombre del servicio se normaliza (trim + primera
     *   letra en mayuscula) para evitar duplicados por diferencia de formato.
     */
    public Servicio save(Servicio servicio) {

        // R1: Precio debe ser positivo
        if (servicio.getPrecio() != null && servicio.getPrecio() <= 0) {
            logger.error("Intento de guardar servicio con precio invalido: {}", servicio.getPrecio());
            throw new BadRequestException(
                    "El precio del servicio debe ser mayor a cero. Valor recibido: " +
                    servicio.getPrecio());
        }

        // R2: Duracion debe ser positiva
        if (servicio.getDuracion() != null && servicio.getDuracion() <= 0) {
            logger.error("Intento de guardar servicio con duracion invalida: {}", servicio.getDuracion());
            throw new BadRequestException(
                    "La duracion del servicio debe ser mayor a cero minutos. Valor recibido: " +
                    servicio.getDuracion());
        }

        // R3: Normalizar nombre del servicio
        if (servicio.getNombre() != null && !servicio.getNombre().isBlank()) {
            String nombreNormalizado = servicio.getNombre().trim();
            nombreNormalizado = nombreNormalizado.substring(0, 1).toUpperCase()
                    + nombreNormalizado.substring(1).toLowerCase();
            servicio.setNombre(nombreNormalizado);
        }

        logger.info("Guardando servicio: '{}' - precio: {} - duracion: {} min",
                servicio.getNombre(), servicio.getPrecio(), servicio.getDuracion());

        return repository.save(servicio);
    }

    public void delete(Long id) {

        logger.info("Eliminando servicio con ID {}", id);

        if (!repository.existsById(id)) {

            logger.warn("Intento de eliminar servicio inexistente {}", id);

            throw new ResourceNotFoundException(
                    "Servicio con ID " + id + " no encontrado");
        }

        repository.deleteById(id);

        logger.info("Servicio {} eliminado correctamente", id);
    }

    public boolean existsById(Long id) {

        logger.info("Verificando existencia del servicio {}", id);

        return repository.existsById(id);
    }
}