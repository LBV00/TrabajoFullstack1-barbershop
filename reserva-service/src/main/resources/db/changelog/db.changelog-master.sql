--liquibase formatted sql

--changeset tu_nombre:1
CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    total DOUBLE NOT NULL
);

CREATE TABLE detalle_reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reserva_id BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    CONSTRAINT fk_reserva FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE CASCADE
);

--changeset tu_nombre:2
-- Insertamos forzando el ID (1,2,3,4,5) para que no falle la Clave Foránea
INSERT INTO reservas (id, id_usuario, fecha_reserva, total) VALUES
(1, 1, '2026-05-12 10:00:00', 15000.0),
(2, 2, '2026-05-13 11:30:00', 12000.0),
(3, 1, '2026-05-14 14:00:00', 25000.0),
(4, 3, '2026-05-15 16:15:00', 15000.0),
(5, 4, '2026-05-16 09:00:00', 18000.0);

INSERT INTO detalle_reservas (reserva_id, id_producto, precio_unitario) VALUES
(1, 1, 15000.0),
(2, 2, 12000.0),
(3, 3, 25000.0),
(4, 1, 15000.0),
(5, 4, 18000.0);