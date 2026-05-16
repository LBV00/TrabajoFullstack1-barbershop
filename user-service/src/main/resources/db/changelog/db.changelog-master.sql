--liquibase formatted sql

--changeset developer:1
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    gmail VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20)
);
--changeset developer:2
INSERT INTO users (rut, nombre, apellido, gmail, telefono) VALUES
('22456792-8', 'ronald', 'abarzua', 'ronalA@gmail.com', '9-91706569'),
('21744878-2', 'Maria', 'Gomez', 'maria@gmail.com', '9-61706569'),
('22938964-3', 'Carlos', 'Lopez', 'carlos@gmail.com', '3-917061293'),
('21383627-4', 'Ana', 'Martinez', 'ana@gmail.com', '9-51707569'),
('20834756-5', 'Luis', 'Fernandez', 'luis@gmail.com', '9-121702569'),
('21475665-6', 'Laura', 'Garcia', 'laura@gmail.com', '9-01406569'),
('19237466-7', 'Diego', 'Rodriguez', 'diego@gmail.com', '9-91098469'),
('22938656-8', 'Sofia', 'Romero', 'sofia@gmail.com', '9-91706875'),
('21937556-9', 'Andres', 'Herrera', 'andres@gmail.com', '9-23406569'),
('20238461-0', 'Paula', 'Castro', 'paula@gmail.com', '9-91707869');