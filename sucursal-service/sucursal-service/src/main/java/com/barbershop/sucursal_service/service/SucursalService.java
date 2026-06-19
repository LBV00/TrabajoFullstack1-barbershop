package com.barbershop.sucursal_service.service;

import com.barbershop.sucursal_service.model.Sucursal;
import com.barbershop.sucursal_service.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<Sucursal> findAll() {
        return repository.findAll();
    }

    public Sucursal findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Sucursal save(Sucursal sucursal) {
        return repository.save(sucursal);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}