--liquibase formatted sql

--changeset developer:1
CREATE TABLE inventarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    stock INT NOT NULL,
    ubicacion VARCHAR(100)
);

--changeset developer:2
INSERT INTO inventarios (producto_id, stock, ubicacion) VALUES
(1, 20, 'Bodega Central'),
(2, 15, 'Sucursal La Reina'),
(3, 40, 'Sucursal Ñuñoa'),
(4, 10, 'Sucursal Maipu'),
(5, 25, 'Bodega Central');