package com.barbershop.pago_service.service;

import com.barbershop.pago_service.model.Pago;
import com.barbershop.pago_service.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> findAll() { return pagoRepository.findAll(); }

    public Optional<Pago> findById(Long id) { return pagoRepository.findById(id); }

    public Pago save(Pago pago) {
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }
        return pagoRepository.save(pago);
    }

    public void deleteById(Long id) { pagoRepository.deleteById(id); }
}