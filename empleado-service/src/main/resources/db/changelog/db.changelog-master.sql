--liquibase formatted sql

--changeset developer:1
CREATE TABLE empleados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    disponible BOOLEAN
);

--changeset developer:2
INSERT INTO empleados (nombre, especialidad, telefono, disponible) VALUES
('Martin Lara','Barbero Senior','987654321',true),
('Lucas Bustamante','Fade Specialist','912345678',true),
('Carlos Soto','Barbero','923456789',false),
('Diego Rojas','Barbero','934567891',true),
('Javier Perez','Barbero Senior','945678912',true);