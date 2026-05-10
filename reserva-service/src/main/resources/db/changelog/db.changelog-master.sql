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