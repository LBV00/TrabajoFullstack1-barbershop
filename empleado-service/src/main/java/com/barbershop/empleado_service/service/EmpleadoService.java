package com.barbershop.empleado_service.service;

import com.barbershop.empleado_service.exception.BadRequestException;
import com.barbershop.empleado_service.exception.ResourceNotFoundException;
import com.barbershop.empleado_service.model.Empleado;
import com.barbershop.empleado_service.repository.EmpleadoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmpleadoService.class);

    @Autowired
    private EmpleadoRepository repository;

    public List<Empleado> findAll() {

        logger.info("Listando todos los empleados");

        return repository.findAll();
    }

    public Empleado findById(Long id) {

        logger.info("Buscando empleado con ID {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {

                    logger.warn("Empleado con ID {} no encontrado", id);

                    return new ResourceNotFoundException(
                            "Empleado con ID " + id + " no encontrado");
                });
    }

  
    public Empleado save(Empleado empleado) {

        // R1: Valor por defecto para disponible en nuevos empleados
        if (empleado.getId() == null && empleado.getDisponible() == null) {
            empleado.setDisponible(true);
            logger.info("Empleado nuevo: disponibilidad establecida en true por defecto");
        }

        // R3: No se permite registrar un empleado marcado como no disponible
        if (Boolean.FALSE.equals(empleado.getDisponible()) && empleado.getId() == null) {
            logger.warn("Intento de crear empleado con disponible=false rechazado");
            throw new BadRequestException(
                    "No se puede registrar un empleado con disponibilidad en false. " +
                    "Use el metodo de actualizacion para cambiar su estado.");
        }

        // R2: Normalizar especialidad a mayúsculas
        if (empleado.getEspecialidad() != null) {
            empleado.setEspecialidad(empleado.getEspecialidad().toUpperCase().trim());
        }

        logger.info("Guardando empleado: {} - especialidad: {} - disponible: {}",
                empleado.getNombre(), empleado.getEspecialidad(), empleado.getDisponible());

        return repository.save(empleado);
    }

    public void delete(Long id) {

        logger.info("Eliminando empleado con ID {}", id);

        if (!repository.existsById(id)) {

            logger.warn(
                    "Intento de eliminar empleado inexistente {}",
                    id);

            throw new ResourceNotFoundException(
                    "Empleado con ID " + id + " no encontrado");
        }

        repository.deleteById(id);

        logger.info("Empleado {} eliminado correctamente", id);
    }

    public boolean existsById(Long id) {

        logger.info(
                "Verificando existencia de empleado {}",
                id);

        return repository.existsById(id);
    }
}