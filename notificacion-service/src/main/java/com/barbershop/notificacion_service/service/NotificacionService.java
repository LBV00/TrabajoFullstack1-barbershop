package com.barbershop.notificacion_service.service;

import com.barbershop.notificacion_service.exception.ResourceNotFoundException;
import com.barbershop.notificacion_service.model.Notificacion;
import com.barbershop.notificacion_service.repository.NotificacionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository repository;

    public List<Notificacion> findAll() {

        logger.info("Listando todas las notificaciones");

        return repository.findAll();
    }

    public Notificacion findById(Long id) {

        logger.info("Buscando notificación con ID {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {

                    logger.warn("Notificación con ID {} no encontrada", id);

                    return new ResourceNotFoundException(
                            "Notificación con ID " + id + " no encontrada");
                });
    }

    public Notificacion save(Notificacion notificacion) {

        logger.info("Guardando notificación");

        return repository.save(notificacion);
    }

    public void delete(Long id) {

        logger.info("Eliminando notificación con ID {}", id);

        if (!repository.existsById(id)) {

            logger.warn(
                    "Intento de eliminar notificación inexistente {}",
                    id);

            throw new ResourceNotFoundException(
                    "Notificación con ID " + id + " no encontrada");
        }

        repository.deleteById(id);

        logger.info("Notificación {} eliminada correctamente", id);
    }

    public boolean existsById(Long id) {

        logger.info(
                "Verificando existencia de notificación {}",
                id);

        return repository.existsById(id);
    }
}