--liquibase formatted sql

--changeset developer:1
CREATE TABLE sucursales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(20)
);

--changeset developer:2
INSERT INTO sucursales (nombre, direccion, telefono) VALUES
('Sucursal La Reina', 'Av. Ossa 123', '987654321'),
('Sucursal Ñuñoa', 'Irarrazaval 500', '912345678'),
('Sucursal Maipu', 'Pajaritos 1200', '923456789'),
('Sucursal Puente Alto', 'Concha y Toro 456', '934567891'),
('Sucursal Providencia', 'Manuel Montt 890', '945678912'),
('Sucursal Las Condes', 'Apoquindo 1500', '956789123'),
('Sucursal Santiago Centro', 'Alameda 100', '967891234'),
('Sucursal La Florida', 'Vicuña Mackenna 800', '978912345'),
('Sucursal Quilicura', 'Lo Cruzat 300', '989123456'),
('Sucursal San Bernardo', 'Colón Sur 250', '991234567');

