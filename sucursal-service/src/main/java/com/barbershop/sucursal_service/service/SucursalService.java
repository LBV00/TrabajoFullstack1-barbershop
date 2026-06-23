package com.barbershop.sucursal_service.service;

import com.barbershop.sucursal_service.exception.ResourceNotFoundException;
import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.repository.SucursalRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private static final Logger logger =
            LoggerFactory.getLogger(SucursalService.class);

    @Autowired
    private SucursalRepository sucursalRepository;

    public List<Sucursal> findAll() {

        logger.info("Listando todas las sucursales");

        return sucursalRepository.findAll();
    }

    public Sucursal findById(Long id) {

        logger.info("Buscando sucursal con ID {}", id);

        return sucursalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Sucursal con ID {} no encontrada", id);
                    return new ResourceNotFoundException(
                            "Sucursal con ID " + id + " no encontrada");
                });
    }

    public Sucursal save(Sucursal sucursal) {

        logger.info("Guardando sucursal");

        return sucursalRepository.save(sucursal);
    }

    public void delete(Long id) {

        logger.info("Eliminando sucursal con ID {}", id);

        if (!sucursalRepository.existsById(id)) {

            logger.warn("Intento de eliminar sucursal inexistente {}", id);

            throw new ResourceNotFoundException(
                    "Sucursal con ID " + id + " no encontrada");
        }

        sucursalRepository.deleteById(id);

        logger.info("Sucursal {} eliminada correctamente", id);
    }

    public boolean existsById(Long id) {

        logger.info("Verificando existencia de sucursal {}", id);

        return sucursalRepository.existsById(id);
    }
}