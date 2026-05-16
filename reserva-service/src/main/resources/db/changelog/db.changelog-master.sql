--liquibase formatted sql

--changeset developer:1
CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    total DOUBLE NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE'
);

CREATE TABLE detalle_reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reserva_id BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE CASCADE
);


--changeset tu_nombre:2
INSERT INTO reservas (id_usuario, fecha_reserva, total) VALUES
(1, '2026-05-12 10:00:00', 15000.0),
(2, '2026-05-13 11:30:00', 12000.0),
(1, '2026-05-14 14:00:00', 25000.0),
(3, '2026-05-15 16:15:00', 15000.0),
(4, '2026-05-16 09:00:00', 18000.0);

INSERT INTO detalle_reservas (reserva_id, id_producto, precio) VALUES
(1, 1, 15000.0),
(2, 2, 12000.0),
(3, 3, 25000.0),
(4, 1, 15000.0),
(5, 4, 18000.0);