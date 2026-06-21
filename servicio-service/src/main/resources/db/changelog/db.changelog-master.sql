--liquibase formatted sql

--changeset developer:1
CREATE TABLE servicios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    precio DOUBLE NOT NULL,
    duracion INT NOT NULL
);

--changeset developer:2
INSERT INTO servicios (nombre, descripcion, precio, duracion) VALUES
('Corte Clasico','Corte tradicional',8000,30),
('Fade','Degradado profesional',12000,45),
('Perfilado de Barba','Perfilado completo',7000,20),
('Corte + Barba','Servicio completo',18000,60),
('Lavado Capilar','Lavado profesional',5000,15);