# 💈 BarberShop — Arquitectura de Microservicios

Sistema de gestión integral para una barbería, construido sobre una arquitectura distribuida de microservicios con Spring Boot. Permite administrar usuarios, reservas, productos, pagos y reseñas, con autenticación JWT centralizada en el API Gateway.

---

## 👥 Integrantes

| Nombre | Rol |
|--------|-----|
| Luca Buitano | 
| Martin Lara |

---

## 🛠️ Tecnologías

| Tecnología | Uso |
|-----------|-----|
| Java 17 | Lenguaje principal |
| Spring Boot 4.0.6 | Framework base de cada microservicio |
| Spring Cloud Gateway | API Gateway reactivo |
| Spring Data JPA + Hibernate | Persistencia y ORM |
| MySQL | Base de datos relacional |
| Liquibase | Migraciones y datos iniciales |
| Spring WebFlux (WebClient) | Comunicación entre microservicios |
| JJWT 0.12.6 | Generación y validación de tokens JWT |
| BCrypt | Cifrado de contraseñas |
| Lombok | Reducción de boilerplate |
| Jakarta Bean Validation | Validaciones con anotaciones |
| SLF4J | Logging estructurado |

---

## 🏗️ Arquitectura



Cada microservicio tiene su **propia base de datos MySQL** independiente. La comunicación entre servicios es síncrona mediante WebClient a través del Gateway.


## 📦 Microservicios

| Servicio | Puerto | Base de datos | Descripción |
|----------|--------|---------------|-------------|
| api-gateway | 7090 | — | Enrutamiento y validación JWT |
| auth-service | 7096 | db_barbershop_auth | Login y generación de tokens JWT |
| user-service | 7091 | db_barbershop_users | Gestión de clientes |
| reserva-service | 7092 | db_barbershop_reservas | Agendamiento de horas (2 tablas relacionadas) |
| producto-service | 7093 | db_barbershop_productos | Catálogo y stock |
| pago-service | 7094 | db_barbershop_pagos | Registro de transacciones |
| resena-service | 7095 | db_barbershop_resenas | Calificaciones y feedback |

### Comunicación entre servicios

- **pago-service** consulta a `user-service` y `reserva-service` antes de registrar un pago (valida existencia de ambos).
- **reserva-service** consulta a `user-service` antes de crear una reserva.
- **resena-service** consulta a `user-service` antes de crear una reseña.

---

## ⚙️ Requisitos previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- VS Code o IntelliJ IDEA

---

## 🚀 Pasos para ejecutar



### 2. Crear las bases de datos en MySQL

*los esquemas se crean automáticamente con `createDatabaseIfNotExist=true`*



### 4. Ejecutar los microservicios



## 🔐 Autenticación JWT

Todos los endpoints (excepto `/auth/login`) requieren un token JWT en el header.

### Obtener token

```
POST http://localhost:7090/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "1234"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "rol": "ADMIN",
  "mensaje": "Login exitoso"
}
```

### Usar el token

En Postman: tab `Authorization` → tipo `Bearer Token` → pegar el token.

O en el header directamente:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Usuarios de prueba (contraseña: `1234`)

| Username | Rol |
|----------|-----|
| admin | ADMIN |
| barbero1 | USER |
| cliente1 | USER |
| gerente | ADMIN |

---

## 📋 Endpoints por microservicio

### 👤 user-service — `/users`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/users` | Listar todos los usuarios |
| GET | `/users/{id}` | Buscar usuario por ID |
| POST | `/users` | Crear usuario |
| PUT | `/users/{id}` | Actualizar usuario |
| DELETE | `/users/{id}` | Eliminar usuario |
| GET | `/users/{id}/exists` | Verificar existencia (uso interno) |

**Body POST/PUT:**
```json
{
  "rut": "11111111-1",
  "nombre": "Juan",
  "apellido": "Perez",
  "gmail": "juan.perez@example.com",
  "telefono": "+56912345678"
}
```

---

### 📅 reserva-service — `/reservas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/reservas` | Listar todas las reservas |
| GET | `/reservas/{id}` | Buscar reserva por ID |
| GET | `/reservas/cliente/{idUsuario}` | Reservas por cliente |
| GET | `/reservas/total` | Total de reservas registradas |
| POST | `/reservas` | Crear reserva |
| PUT | `/reservas/{id}` | Actualizar reserva |
| DELETE | `/reservas/{id}` | Eliminar reserva |
| GET | `/reservas/{id}/exists` | Verificar existencia (uso interno) |

**Body POST/PUT:**
```json
{
  "idUsuario": 1,
  "fechaReserva": "2026-06-01T15:30:00",
  "total": 15000.0,
  "estado": "PENDIENTE"
}
```

---

### 🧴 producto-service — `/productos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/productos` | Listar todos los productos |
| GET | `/productos/{id}` | Buscar producto por ID |
| POST | `/productos` | Crear producto |
| PUT | `/productos/{id}` | Actualizar producto |
| DELETE | `/productos/{id}` | Eliminar producto |

**Body POST/PUT:**
```json
{
  "nombre": "Cera modeladora mate",
  "precio": 8500.0,
  "stock": 20
}
```

---

### 💳 pago-service — `/pagos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/pagos` | Listar todos los pagos |
| GET | `/pagos/{id}` | Buscar pago por ID |
| GET | `/pagos/usuario/{idUsuario}` | Pagos por usuario |
| GET | `/pagos/total` | Total de pagos registrados |
| POST | `/pagos` | Registrar pago (valida usuario y reserva) |
| PUT | `/pagos/{id}` | Actualizar pago |
| DELETE | `/pagos/{id}` | Eliminar pago |

**Body POST/PUT:**
```json
{
  "idUsuario": 1,
  "idReserva": 1,
  "monto": 15000.0,
  "metodoPago": "TARJETA DE CREDITO"
}
```

> Si `idUsuario` o `idReserva` no existen, retorna `400 Bad Request` con mensaje de error.

---

### ⭐ resena-service — `/resenas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/resenas` | Listar todas las reseñas |
| GET | `/resenas/{id}` | Buscar reseña por ID |
| GET | `/resenas/usuario/{idUsuario}` | Reseñas por usuario |
| GET | `/resenas/total` | Total de reseñas registradas |
| POST | `/resenas` | Crear reseña (valida usuario) |
| PUT | `/resenas/{id}` | Actualizar reseña |
| DELETE | `/resenas/{id}` | Eliminar reseña |

**Body POST:**
```json
{
  "idUsuario": 1,
  "idReserva": 1,
  "calificacion": 5,
  "comentario": "Excelente servicio, muy recomendado."
}
```

**Body PUT:**
```json
{
  "calificacion": 4,
  "comentario": "Muy buen servicio."
}
```

---

## 🗄️ Modelo de datos

### reserva-service (2 tablas relacionadas)

```
reservas
├── id (PK)
├── id_usuario (FK lógica → user-service)
├── fecha_reserva
├── total
└── estado

detalle_reservas
├── id (PK)
├── reserva_id (FK → reservas.id)
├── id_producto
└── precio_unitario
```

---

## ✅ Funcionalidades implementadas

- [x] 6 microservicios independientes con base de datos propia
- [x] Patrón CSR (Controller / Service / Repository) en todos los servicios
- [x] CRUD completo en todos los microservicios
- [x] Liquibase en todos los servicios con datos iniciales (10+ registros)
- [x] Relación @OneToMany / @ManyToOne en reserva-service
- [x] Validaciones Bean Validation con DTOs separados de entidades
- [x] Comunicación entre microservicios con WebClient
- [x] Manejo de excepciones con @RestControllerAdvice en todos los servicios
- [x] Logging estructurado con SLF4J
- [x] Endpoints adicionales: búsqueda por atributo, totales
- [x] API Gateway con enrutamiento centralizado
- [x] Autenticación JWT (bono)

---

## 🔗 Repositorio

[https://github.com/LBV00/TrabajoFullstack1-barbershop](https://github.com/LBV00/TrabajoFullstack1-barbershop)
