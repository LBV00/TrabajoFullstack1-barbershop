--liquibase formatted sql

--changeset developer:1
CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_reserva BIGINT NOT NULL,
    monto DOUBLE NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP
);

--changeset developer:2
INSERT INTO pagos (id_usuario, id_reserva, monto, metodo_pago, fecha_pago) VALUES
(1, 1, 15000, 'TARJETA DE CREDITO', '2026-05-20 10:30:00'),
(2, 2, 8000, 'EFECTIVO', '2026-05-20 11:45:00'),
(1, 3, 20000, 'TRANSFERENCIA', '2026-05-21 15:20:00');