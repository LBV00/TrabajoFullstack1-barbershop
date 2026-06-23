# Inventario Service

## Descripción

Microservicio encargado del control y administración del inventario.

## Integrantes

- Martín Lara
- Luca Buitano

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA
- MySQL
- Liquibase
- Swagger/OpenAPI
- Docker
- JUnit
- Mockito
- HATEOAS

## Arquitectura

- Controller
- Service
- Repository
- DTO
- Model
- Config
- Exception

## Funcionalidades

- Crear registro
- Obtener inventario
- Obtener registro por ID
- Actualizar registro
- Eliminar registro

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /inventarios |
| GET | /inventarios/{id} |
| POST | /inventarios |
| PUT | /inventarios/{id} |
| DELETE | /inventarios/{id} |

## Swagger

http://localhost:7098/swagger-ui.html

## Base de Datos

MySQL

## Características Implementadas

- CRUD Completo
- DTO
- Validaciones
- HATEOAS
- Swagger/OpenAPI
- Logging
- Manejo Global de Excepciones
- Docker
- Liquibase
- Pruebas Unitarias

## Ejecución Local

mvn spring-boot:run

## Docker

docker compose up --build