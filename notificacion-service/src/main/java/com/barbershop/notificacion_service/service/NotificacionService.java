package com.barbershop.notificacion_service.service;

import com.barbershop.notificacion_service.model.Notificacion;
import com.barbershop.notificacion_service.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    public List<Notificacion> findAll() {
        return repository.findAll();
    }

    public Notificacion findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Notificacion save(Notificacion notificacion) {
        return repository.save(notificacion);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}