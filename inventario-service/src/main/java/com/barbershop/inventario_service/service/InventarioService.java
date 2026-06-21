package com.barbershop.inventario_service.service;

import com.barbershop.inventario_service.model.Inventario;
import com.barbershop.inventario_service.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository repository;

    public List<Inventario> findAll() {
        return repository.findAll();
    }

    public Inventario findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Inventario save(Inventario inventario) {
        return repository.save(inventario);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}