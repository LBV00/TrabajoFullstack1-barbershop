# Empleado Service

## Descripción

Microservicio encargado de la gestión de empleados de la barbería.

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

- Crear empleado
- Obtener empleados
- Obtener empleado por ID
- Actualizar empleado
- Eliminar empleado

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /empleados |
| GET | /empleados/{id} |
| POST | /empleados |
| PUT | /empleados/{id} |
| DELETE | /empleados/{id} |

## Swagger

http://localhost:7097/swagger-ui.html

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