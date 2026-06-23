# Auth Service

## Descripción

Microservicio encargado de la autenticación y autorización de usuarios dentro del sistema Barbershop. Gestiona el registro de usuarios, validación de credenciales y generación de tokens JWT utilizados para proteger los recursos del sistema.

## Integrantes

- Martín Lara
- Luca Buitano

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA
- MySQL
- Liquibase
- JWT
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

- Registro de usuarios
- Inicio de sesión
- Generación de JWT
- Validación de credenciales

## Endpoints Principales

| Método | Endpoint |
|----------|----------|
| POST | /auth/login |
| POST | /auth/register |

## Swagger

http://localhost:7096/swagger-ui.html

## Base de Datos

MySQL

## Características Implementadas

- DTO
- Validaciones
- JWT
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
JWT_SECRET
```

## Ejecución Local

```bash
mvn spring-boot:run
```

## Docker

```bash
docker compose up --build
```