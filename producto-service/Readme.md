# Producto Service

## Descripción

Microservicio encargado de la administración de productos disponibles dentro del sistema Barbershop.

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

- Crear producto
- Obtener productos
- Obtener producto por ID
- Actualizar producto
- Eliminar producto

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /productos |
| GET | /productos/{id} |
| POST | /productos |
| PUT | /productos/{id} |
| DELETE | /productos/{id} |

## Swagger

http://localhost:7093/swagger-ui.html

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