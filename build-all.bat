@echo off
echo ==========================================
echo Compilando y empaquetando microservicios...
echo ==========================================

set SERVICES=api-gateway auth-service empleado-service inventario-service notificacion-service pago-service producto-service resena-service reserva-service servicio-service sucursal-service user-service

for %%s in (%SERVICES%) do (
    echo.
    echo --- Empaquetando %%s ---
    cd %%s
    call mvnw.cmd clean package -DskipTests
    if errorlevel 1 (
        echo Error al compilar %%s
        cd ..
        exit /b 1
    )
    cd ..
)

echo.
echo ==========================================
echo Construyendo y levantando contenedores Docker...
echo ==========================================
docker-compose up -d --build

echo.
echo ==========================================
echo ¡Proceso finalizado con éxito! Todos los servicios están corriendo.
echo ==========================================
