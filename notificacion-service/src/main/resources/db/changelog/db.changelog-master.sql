--liquibase formatted sql

--changeset developer:1
CREATE TABLE notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    destinatario VARCHAR(150) NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    estado VARCHAR(50)
);

--changeset developer:2
INSERT INTO notificaciones (destinatario, mensaje, estado) VALUES
('martin@gmail.com','Reserva confirmada','ENVIADA'),
('cliente1@gmail.com','Pago recibido','ENVIADA'),
('cliente2@gmail.com','Recordatorio de cita','PENDIENTE'),
('cliente3@gmail.com','Reserva cancelada','ENVIADA'),
('cliente4@gmail.com','Promoción disponible','PENDIENTE');