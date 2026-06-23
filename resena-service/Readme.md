# Resena Service

## Descripción

Microservicio encargado de almacenar y gestionar las reseñas realizadas por los clientes.

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

- Crear reseña
- Obtener reseñas
- Obtener reseña por ID
- Actualizar reseña
- Eliminar reseña

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /resenas |
| GET | /resenas/{id} |
| POST | /resenas |
| PUT | /resenas/{id} |
| DELETE | /resenas/{id} |

## Swagger

http://localhost:7095/swagger-ui.html

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