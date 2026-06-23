# Notificacion Service

## Descripción

Microservicio encargado de gestionar las notificaciones generadas por el sistema.

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

- Crear notificación
- Obtener notificaciones
- Obtener notificación por ID
- Actualizar notificación
- Eliminar notificación

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /notificaciones |
| GET | /notificaciones/{id} |
| POST | /notificaciones |
| PUT | /notificaciones/{id} |
| DELETE | /notificaciones/{id} |

## Swagger

http://localhost:7099/swagger-ui.html

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