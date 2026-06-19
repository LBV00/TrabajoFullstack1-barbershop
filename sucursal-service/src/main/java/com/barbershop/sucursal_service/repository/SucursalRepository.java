package com.barbershop.sucursal_service.repository;

import com.barbershop.sucursal_service.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository
        extends JpaRepository<Sucursal, Long> {
}