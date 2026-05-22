package com.barbershop.auth_service.service;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

@Service
public class HashService {

    public String sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to compute SHA-1", e);
        }
    }
}