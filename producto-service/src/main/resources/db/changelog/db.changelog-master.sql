--liquibase formatted sql

--changeset developer:1
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    descripcion VARCHAR(200)
);

--changeset developer:2
INSERT INTO productos (nombre, precio, descripcion) VALUES
('Corte Clásico', 15000, 'Corte de cabello tradicional a tijera o máquina'),
('Perfilado de Barba', 8000, 'Perfilado con navaja, vapor y toalla caliente'),
('Corte + Barba (Combo)', 20000, 'Servicio completo de corte de cabello y arreglo de barba'),
('Degradado (Fade)', 17000, 'Corte moderno con degradado a los lados'),
('Corte de Niño', 12000, 'Corte clásico o moderno para menores de 12 años'),
('Lavado y Peinado', 5000, 'Lavado con shampoo premium y peinado con cera o pomada'),
('Tinte o Decoloración', 25000, 'Coloración completa o mechas para el cabello'),
('Limpieza Facial', 10000, 'Limpieza profunda con exfoliación y mascarilla negra exfoliante'),
('Corte a Navaja', 18000, 'Corte completo utilizando navaja clásica para un estilo retro'),
('Diseño/Líneas (Hair Tattoo)', 7000, 'Diseños, grecas y líneas marcadas en el corte');