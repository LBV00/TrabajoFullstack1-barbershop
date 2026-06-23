# Pago Service

## Descripción

Microservicio encargado de gestionar los pagos realizados por los clientes.

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

- Registrar pago
- Obtener pagos
- Obtener pago por ID
- Actualizar pago
- Eliminar pago

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /pagos |
| GET | /pagos/{id} |
| POST | /pagos |
| PUT | /pagos/{id} |
| DELETE | /pagos/{id} |

## Swagger

http://localhost:7094/swagger-ui.html

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