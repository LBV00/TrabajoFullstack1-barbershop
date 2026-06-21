package com.barbershop.inventario_service.repository;

import com.barbershop.inventario_service.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository
        extends JpaRepository<Inventario, Long> {
}