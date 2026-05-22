--liquibase formatted sql

--changeset auth:1
CREATE TABLE IF NOT EXISTS usuarios_auth (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    rol        VARCHAR(20)  NOT NULL DEFAULT 'USER',
    activo     BOOLEAN      NOT NULL DEFAULT TRUE
);

--changeset auth:2
INSERT INTO usuarios_auth (username, password, rol) VALUES ('admin', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'ADMIN');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('barbero1', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('barbero2', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('cliente1', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('cliente2', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('gerente', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'ADMIN');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('recep1', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('recep2', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('barbero3', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');
INSERT INTO usuarios_auth (username, password, rol) VALUES ('barbero4', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', 'USER');