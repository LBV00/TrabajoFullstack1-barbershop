--liquibase formatted sql

--changeset developer:1
CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    precio DOUBLE,
    stock INT
);

--changeset developer:2
INSERT INTO producto (nombre, precio, stock) VALUES
('Cera moldeadora mate', 8500, 20),
('Aceite hidratante para barba', 12000, 15),
('Gel transparente para afeitar', 6000, 30),
('Shampoo especial barba y cabello', 7500, 25),
('Loción Aftershave', 15000, 10),
('Pomada de fijación fuerte', 9000, 18),
('Peine de madera clásico', 4000, 40),
('Cepillo de cerdas de jabalí', 11000, 12),
('Polvos texturizadores', 10500, 22),
('Tónico para crecimiento de barba', 18000, 8);