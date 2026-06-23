# Servicio Service

## Descripción

Microservicio encargado de administrar los servicios ofrecidos por la barbería.

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

- Crear servicio
- Obtener servicios
- Obtener servicio por ID
- Actualizar servicio
- Eliminar servicio

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /servicios |
| GET | /servicios/{id} |
| POST | /servicios |
| PUT | /servicios/{id} |
| DELETE | /servicios/{id} |

## Swagger

http://localhost:7100/swagger-ui.html

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