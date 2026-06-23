package com.barbershop.inventario_service.service;

import com.barbershop.inventario_service.exception.ResourceNotFoundException;
import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.repository.InventarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private static final Logger logger =
            LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository repository;

    public List<Inventario> findAll() {

        logger.info("Listando todos los inventarios");

        return repository.findAll();
    }

    public Inventario findById(Long id) {

        logger.info("Buscando inventario con ID {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {

                    logger.warn("Inventario con ID {} no encontrado", id);

                    return new ResourceNotFoundException(
                            "Inventario con ID " + id + " no encontrado");
                });
    }

    public Inventario save(Inventario inventario) {

        logger.info("Guardando inventario");

        return repository.save(inventario);
    }

    public void delete(Long id) {

        logger.info("Eliminando inventario con ID {}", id);

        if (!repository.existsById(id)) {

            logger.warn(
                    "Intento de eliminar inventario inexistente {}",
                    id);

            throw new ResourceNotFoundException(
                    "Inventario con ID " + id + " no encontrado");
        }

        repository.deleteById(id);

        logger.info("Inventario {} eliminado correctamente", id);
    }

    public boolean existsById(Long id) {

        logger.info(
                "Verificando existencia de inventario {}",
                id);

        return repository.existsById(id);
    }
}