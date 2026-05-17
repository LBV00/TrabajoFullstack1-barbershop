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
(5, '2026-05-20 09:15:00', 12000.0, 'CONFIRMADA'),
(6, '2026-05-21 11:30:00', 18000.0, 'COMPLETADA'),
(7, '2026-05-22 14:00:00', 22000.0, 'PENDIENTE'),
(8, '2026-05-23 16:45:00', 15000.0, 'CONFIRMADA'),
(9, '2026-05-24 10:15:00', 28000.0, 'COMPLETADA'),
(10, '2026-05-25 13:00:00', 35000.0, 'CONFIRMADA'),
(1, '2026-06-26 15:00:00', 15000.0, 'PENDIENTE'),
(2, '2026-06-27 17:30:00', 10000.0, 'COMPLETADA');

--changeset barbershop:4
INSERT INTO detalle_reservas (reserva_id, id_producto, precio_unitario) VALUES
(1, 1, 15000.0),
(2, 2, 25000.0),
(3, 1, 10000.0),
(4, 3, 30000.0),
(5, 1, 12000.0),
(6, 2, 18000.0),
(7, 3, 22000.0),
(8, 1, 15000.0),
(9, 2, 28000.0),
(10, 3, 35000.0),
(11, 1, 15000.0),
(12, 1, 10000.0);