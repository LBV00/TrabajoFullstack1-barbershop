package com.barbershop.sucursal_service.service;

import com.barbershop.sucursal_service.exception.ResourceNotFoundException;
import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.repository.SucursalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    public List<Sucursal> findAll() {
        return sucursalRepository.findAll();
    }

    public Sucursal findById(Long id) {

        return sucursalRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sucursal con ID " + id + " no encontrada"));
    }

    public Sucursal save(Sucursal sucursal) {

        return sucursalRepository.save(sucursal);
    }

    public void delete(Long id) {

        if (!sucursalRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Sucursal con ID " + id + " no encontrada");
        }

        sucursalRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return sucursalRepository.existsById(id);
    }
}