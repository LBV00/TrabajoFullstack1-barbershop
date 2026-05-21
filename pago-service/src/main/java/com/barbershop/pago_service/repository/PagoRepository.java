package com.barbershop.pago_service.repository;

import com.barbershop.pago_service.model.Pago;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
     // Spring Data JPA crea la consulta SQL automáticamente por el nombre del método
    List<Pago> findByIdUsuario(Long idUsuario); 
}