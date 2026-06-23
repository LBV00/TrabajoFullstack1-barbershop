package com.barbershop.servicio_service.service;

import com.barbershop.servicio_service.exception.ResourceNotFoundException;
import com.barbershop.servicio_service.model.Servicio;
import com.barbershop.servicio_service.repository.ServicioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository repository;

    public List<Servicio> findAll() {
        return repository.findAll();
    }

    public Servicio findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Servicio con ID " + id + " no encontrado"));
    }

    public Servicio save(Servicio servicio) {
        return repository.save(servicio);
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Servicio con ID " + id + " no encontrado");
        }

        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}