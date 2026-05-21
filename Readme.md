# 💈 Proyecto BarberShop - Arquitectura de Microservicios con Spring Boot

## 📖 Descripción del Proyecto
Este proyecto es un sistema de gestión para una barbería ("BarberShop") construido bajo una **arquitectura orientada a microservicios**. El sistema permite administrar usuarios (clientes), el catálogo de productos/servicios, el agendamiento de reservas, la gestión de transacciones y el feedback mediante reseñas. Todo el tráfico es enrutado y centralizado a través de un **API Gateway**.

## 👥 Integrantes del Equipo
* Luca Buitano
* Martin Lara

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java 21
* **Framework:** Spring Boot y Spring Cloud (API Gateway)
* **Persistencia:** Spring Data JPA, Hibernate
* **Base de Datos:** MySQL
* **Migraciones de BD:** Liquibase (Scripteo e inserción de datos iniciales)
* **Comunicación Síncrona:** Spring WebFlux (WebClient)
* **Herramientas extra:** Lombok, Postman, Jakarta Bean Validation (JSR 380)

## 🏗️ Arquitectura de Microservicios
El sistema se compone de 5 microservicios independientes conectados a través de un API Gateway configurado en el puerto `7090`. Cada microservicio posee su propia base de datos MySQL y no comparten tablas:

1. **API Gateway (Puerto 7090):** Puerta de enlace que enruta las peticiones.
2. **User-Service:** Gestión de clientes (Ruta: `/usuarios/**`).
3. **Reserva-Service:** Agendamiento de horas (Ruta: `/reservas/**`).
4. **Producto-Service:** Catálogo de la vitrina y stock (Ruta: `/productos/**`).
5. **Pago-Service:** Registro de transacciones con validación síncrona (Ruta: `/pagos/**`).
6. **Resena-Service:** Sistema de calificación y feedback de clientes (Ruta: `/resenas/**`).


1. ## Microservicio de Usuarios (user-service)
    *URLs de consulta:*
    **GET (Listar todos): http://localhost:7090/usuarios**
    **GET (Buscar por ID): http://localhost:7090/usuarios/1**
    **DELETE (Eliminar): http://localhost:7090/usuarios/1**
    **JSON para POST (Crear) a http://localhost:7090/usuarios o PUT (Actualizar) a http://localhost:7090/usuarios/1:**
{
    "rut": "11111111-1",
    "nombre": "Juan",
    "apellido": "Perez",
    "gmail": "juan.perez@example.com",
    "telefono": "+56912345678"
}

--------------------------------------------------------------------------------
2. ## Microservicio de Reservas (reserva-service)
    *URLs de consulta:*
    **GET (Listar todas): http://localhost:7090/reservas**
    **GET (Buscar por ID): http://localhost:7090/reservas/1**
    **DELETE (Eliminar): http://localhost:7090/reservas/1**
    **JSON para POST (Crear) a http://localhost:7090/reservas o PUT (Actualizar) a http://localhost:7090/reservas/1:**
{
    "idUsuario": 1,
    "fechaReserva": "2026-06-01T15:30:00",
    "total": 15000.0,
    "estado": "PENDIENTE"
}
--------------------------------------------------------------------------------
3. ## Microservicio de Productos (producto-service)
    **URLs de consulta:**
    **GET (Listar todos): http://localhost:7090/productos**
    **GET (Buscar por ID): http://localhost:7090/productos/1**
    **DELETE (Eliminar): http://localhost:7090/productos/1**
    **JSON para POST (Crear) a http://localhost:7090/productos o PUT (Actualizar) a http://localhost:7090/productos/1:**
{
    "nombre": "Cera modeladora mate",
    "precio": 8500.0,
    "stock": 20
}
--------------------------------------------------------------------------------
4. ## Microservicio de Pagos (pago-service)
    **URLs de consulta:**
    **GET (Listar todos): http://localhost:7090/pagos**
    **GET (Buscar por ID): http://localhost:7090/pagos/1**
    **DELETE (Eliminar): http://localhost:7090/pagos/1**
    **JSON para POST (Crear Pago Exitoso) a http://localhost:7090/pagos o PUT (Actualizar) a http://localhost:7090/pagos/1:**
{
    "idUsuario": 1,
    "idReserva": 1,
    "monto": 15000.0,
    "metodoPago": "TARJETA DE CREDITO"
}
    **JSON para POST (Forzar Error 400 - Usuario Falso) a http://localhost:7090/pagos:**
{
        "idUsuario": 999,
        "idReserva": 1,
        "monto": 15000.0,
        "metodoPago": "EFECTIVO"
}

--------------------------------------------------------------------------------
5. ## Microservicio de Reseñas (resena-service)
    **URLs de consulta:**
    **GET (Listar todas): http://localhost:7090/resenas**
    **GET (Buscar por ID): http://localhost:7090/resenas/1**
    **DELETE (Eliminar): http://localhost:7090/resenas/1**
    **JSON para POST (Crear Reseña) a http://localhost:7090/resenas:**
{
    "idUsuario": 1,
    "idReserva": 1,
    "calificacion": 5,
    "comentario": "Excelente servicio, muy recomendado."
}
    **JSON para PUT (Actualizar Reseña) a http://localhost:7090/resenas/1:**
{
    "idUsuario": 1,
    "idReserva": 1,
    "calificacion": 5,
    "comentario": "Excelente servicio, muy recomendado."
}
