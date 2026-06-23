package com.barbershop.empleado_service.service;

import com.barbershop.empleado_service.exception.ResourceNotFoundException;
import com.barbershop.empleado_service.model.Empleado;
import com.barbershop.empleado_service.repository.EmpleadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repository;

    public List<Empleado> findAll() {
        return repository.findAll();
    }

    public Empleado findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado con ID " + id + " no encontrado"));
    }

    public Empleado save(Empleado empleado) {
        return repository.save(empleado);
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Empleado con ID " + id + " no encontrado");
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}