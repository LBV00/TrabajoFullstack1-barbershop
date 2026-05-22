--liquibase formatted sql

--changeset auth:1
CREATE TABLE IF NOT EXISTS usuarios_auth (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    rol        VARCHAR(20)  NOT NULL DEFAULT 'USER',
    activo     BOOLEAN      NOT NULL DEFAULT TRUE
);

--changeset auth:2

-- la contraseña es simplemente de prueba cambiaar a futuro contraseña :1234--
INSERT INTO usuarios_auth (username, password, rol) VALUES
('admin',    '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'ADMIN'),
('barbero1', '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER'),
('barbero2', '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER'),
('cliente1', '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER'),
('cliente2', '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER'),
('gerente',  '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'ADMIN'),
('recep1',   '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER'),
('recep2',   '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER'),
('barbero3', '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER'),
('barbero4', '$2a$10$7QJ8zV6kL9mN2pX3sY4tOeWqA1bC5dE8fG0hI2jK4lM6nO8pQ0rS2', 'USER');