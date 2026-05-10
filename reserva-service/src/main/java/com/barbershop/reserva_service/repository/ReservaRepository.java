package com.barbershop.reserva_service.repository;
import com.barbershop.reserva_service.model.Reserva;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByIdUsuario(Long idUsuario);
}