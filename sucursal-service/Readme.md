# Sucursal Service

## Descripción

Microservicio encargado de gestionar las sucursales de la barbería.

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

- Crear sucursal
- Obtener sucursales
- Obtener sucursal por ID
- Actualizar sucursal
- Eliminar sucursal

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /sucursales |
| GET | /sucursales/{id} |
| POST | /sucursales |
| PUT | /sucursales/{id} |
| DELETE | /sucursales/{id} |

## Swagger

http://localhost:7101/swagger-ui.html

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