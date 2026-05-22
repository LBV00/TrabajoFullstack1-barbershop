package com.barbershop.user_service.service;

import com.barbershop.user_service.model.User;
import com.barbershop.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        log.info("Listando todos los usuarios.");
        return userRepository.findAll();
    }

    public User findById(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        log.info("Guardando usuario: {}", user.getNombre());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        log.warn("Eliminando usuario con ID: {}", id);
        userRepository.deleteById(id);
    }

    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}