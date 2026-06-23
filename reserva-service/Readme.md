# Reserva Service

## Descripción

Microservicio encargado de la gestión de reservas realizadas por los clientes dentro del sistema Barbershop.

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

- Crear reserva
- Obtener reservas
- Obtener reserva por ID
- Actualizar reserva
- Eliminar reserva

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| GET | /reservas |
| GET | /reservas/{id} |
| POST | /reservas |
| PUT | /reservas/{id} |
| DELETE | /reservas/{id} |

## Swagger

http://localhost:7092/swagger-ui.html

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

## Variables de Entorno

```properties
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

## Ejecución Local

```bash
mvn spring-boot:run
```

## Docker

```bash
docker compose up --build
```