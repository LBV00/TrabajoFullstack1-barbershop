package com.barbershop.servicio_service.service;

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

    public Servicio save(Servicio servicio) {

        logger.info("Guardando servicio");

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