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
(1, 2, 5, 'Impecable como siempre. 100% recomendados.', '2026-05-17 14:00:00'),
(3, 4, 3, 'El servicio fue aceptable, esperaba algo mejor.', '2026-05-19 10:00:00'),
(4, 5, 5, 'El mejor barbero que he visitado. Muy detallista.', '2026-05-20 09:30:00'),
(5, 6, 4, 'Buen corte y ambiente agradable. Volvería.', '2026-05-20 14:00:00'),
(6, 7, 2, 'Esperé demasiado y el resultado fue mediocre.', '2026-05-21 11:00:00'),
(7, 8, 5, 'Servicio impecable. Totalmente recomendado.', '2026-05-21 16:00:00'),
(8, 9, 4, 'Muy buen trabajo, precios justos.', '2026-05-22 12:00:00'),
(9, 10, 5, 'Sin duda el mejor lugar para un corte de cabello.', '2026-05-22 17:30:00');