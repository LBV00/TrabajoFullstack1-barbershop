--liquibase formatted sql

--changeset developer:1
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20)
);
--changeset developer:2
INSERT INTO users (rut, nombre, apellido, email, telefono) VALUES
('11111111-1', 'Juan', 'Perez', 'juan@ejemplo.com', '555-1001'),
('22222222-2', 'Maria', 'Gomez', 'maria@ejemplo.com', '555-1002'),
('33333333-3', 'Carlos', 'Lopez', 'carlos@ejemplo.com', '555-1003'),
('44444444-4', 'Ana', 'Martinez', 'ana@ejemplo.com', '555-1004'),
('55555555-5', 'Luis', 'Fernandez', 'luis@ejemplo.com', '555-1005'),
('66666666-6', 'Laura', 'Garcia', 'laura@ejemplo.com', '555-1006'),
('77777777-7', 'Diego', 'Rodriguez', 'diego@ejemplo.com', '555-1007'),
('88888888-8', 'Sofia', 'Romero', 'sofia@ejemplo.com', '555-1008'),
('99999999-9', 'Andres', 'Herrera', 'andres@ejemplo.com', '555-1009'),
('10101010-0', 'Paula', 'Castro', 'paula@ejemplo.com', '555-1010');