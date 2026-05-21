
--liquibase formatted sql

--changeset barbershop:1
CREATE TABLE resenas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_reserva BIGINT NOT NULL,
    calificacion INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

--changeset barbershop:2
INSERT INTO resenas (id_usuario, id_reserva, calificacion, comentario, fecha_creacion) VALUES
(1, 1, 5, 'Excelente servicio, el barbero fue muy profesional.', '2026-05-16 11:30:00'),
(2, 3, 4, 'Muy buen corte, pero me hicieron esperar 10 minutos.', '2026-05-18 16:00:00'),
(1, 2, 5, 'Impecable como siempre. 100% recomendados.', '2026-05-17 14:00:00');