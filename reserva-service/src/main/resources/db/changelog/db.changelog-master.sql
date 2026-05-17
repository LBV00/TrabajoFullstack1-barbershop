--liquibase formatted sql

--changeset barbershop:1
CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    estado VARCHAR(50) NOT NULL
);

--changeset barbershop:2
CREATE TABLE detalle_reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reserva_id BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE CASCADE
);

--changeset barbershop:3
INSERT INTO reservas (id_usuario, fecha_reserva, total, estado) VALUES
(1, '2026-05-16 10:00:00', 15000.0, 'PENDIENTE'),
(2, '2026-05-17 12:30:00', 25000.0, 'CONFIRMADA'),
(3, '2026-05-18 15:00:00', 10000.0, 'COMPLETADA'),
(4, '2026-05-19 18:00:00', 30000.0, 'CANCELADA'),
(5, '2026-05-20 09:15:00', 12000.0, 'CONFIRMADA');

--changeset barbershop:4
INSERT INTO detalle_reservas (reserva_id, id_producto, precio_unitario) VALUES
(1, 1, 15000.0),
(2, 2, 25000.0),
(3, 1, 10000.0),
(4, 3, 30000.0),
(5, 1, 12000.0);